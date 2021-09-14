package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport
        implements SearchBoardRepository{

    /**
     *  찾는 게시글이 Board 엔티티임을 명시
     */
    public SearchBoardRepositoryImpl() {
        super(Board.class);
    } // 생성자

    @Override
    public Board search1() {
        log.info("Search1.....................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        // QuerydslRepositorySupport는 from절부터 시작.
        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("---------------------------");
        log.info(tuple);
        log.info("---------------------------");

        List<Tuple> result = tuple.fetch();

        return null;
    }

    /**
     *  여러 검색 조건 처리
     */
    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("Search Page........................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null) {
            String[] typeArr = type.split("");

            // 검색 조건 작성
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                } // switch
            } // for
            booleanBuilder.and(conditionBuilder);
        } // if
        tuple.where(booleanBuilder);

        // order by
        Sort sort = pageable.getSort();
        // tuple.orderBy(board.bno.desc());
                              // order : Consumer<>의 매개변수
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String porp = order.getProperty();

            // PathBuilder : 동적 경로 생성을 위한 클래스. (EntityPathBase 클래스를 상속받음.)
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");

            tuple.orderBy(new OrderSpecifier(direction, orderByExpression));
        });
        tuple.groupBy(board);

        // page 처리
        tuple.offset((pageable.getOffset())); // offset : 시작 인덱스 지정
        tuple.limit(pageable.getPageSize());  // limit : 조회할 개수 지정

        List<Tuple> result = tuple.fetch(); // 조회 대상이 여러 건일 경우, collection 반환.

        log.info(result);

        long count = tuple.fetchCount(); // 조회 개수 조회, Long 타입 반환
        log.info("COUNT : " + count);

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,count);
    } // searchPage()


}
