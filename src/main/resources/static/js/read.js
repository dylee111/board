            $(document).ready(function() {
                var bno = [[${dto.bno}]];
                loadJSONData();
                var listGroup = $(".replyList");

                var modal = $(".modal");

<!--
                $(".replyCount").click(function() {
                var listGroup = $(".replyList");
                }) // end click
-->

                <!-- 시간 포맷 설정 -->
                function formatTime(str) {
                    var date = new Date(str);

                    return date.getFullYear() + '/' +
                        ((date.getMonth() + 1) > 10 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1)) + '/' +
                        (date.getDate() > 10 ? date.getDate() : '0' + date.getDate()) + ' ' +
                        (date.getHours() > 10 ? date.getHours() : '0' + date.getHours()) + ':' +
                        (date.getMinutes() > 10 ? date.getMinutes() : '0' + date.getMinutes());
                } // formatTime() end

                <!-- 댓글 정보 JSON 타입으로 불러오기 -->
                function loadJSONData() {
                     $.getJSON('/replies/board/' + bno, function(arr){
                        console.log(arr);

                        var str ="";

                        $(".replyCount").html(" Reply Count   " + arr.length);

                        <!-- 댓글 출력 -->
                        $.each(arr, function(idx, reply) {
                            console.log(reply);
                            str += '   <div class="card-body" data-rno="'+reply.rno+'"><b>'+reply.rno +'</b>';
                            str += '   <h5 class="card-title">'+reply.text+'</h5>';
                            str += '   <h6 class="card-subtitle mb-2 text-muted">'+reply.replyer+'</h6>';
                            str += '   <p class="card-text">'+formatTime(reply.regDate)+'</p>';
                            str += '   </div>';
                        }) // end each
                        listGroup.html(str);
                     }); // end getJSON
                } // loadJSONData() end

                <!-- 댓글 추가 모달 -->
                $(".addReply").click(function() {
                    modal.modal('show');

                    $('input[name="replyText"]').val('');
                    $('input[name="replyer"]').val('');

                    $(".modal-footer .btn").hide();
                    $(".replySave, .replyClose").show();

                }); // addReply end

                <!-- 댓글 저장 -->
                $(".replySave").click(function() {
                    var reply = { bno : bno,
                        text : $('input[name="replyText"]').val(),
                        replyer : $('input[name="replyer"]').val()
                    }
                    console.log(reply);

                    $.ajax({
                        url: '/replies/',
                        method: 'post',
                        data: JSON.stringify(reply),
                        contentType: 'application/json; charset=utf-8',
                        dataType: 'json',

                        success: function(data){
                            console.log(data);

                            var newRno = parseInt(data);

                            alert(newRno + "번 댓글이 등록되었습니다.");
                            modal.modal('hide');
                            loadJSONData();

                        } // success
                    }) // $.ajax
                }); // replySave end

                <!-- 모달 닫기 -->
                $(".replyClose").click(function() { modal.modal('hide'); }); // replyClose end

                <!-- 댓글 클릭 시 모달 띄움 -->
                $('.replyList').on("click", ".card-body", function() {
                    var rno = $(this).data("rno");

                    $('input[name="replyText"]').val($(this).find('.card-title').html());
                    $('input[name="replyer"]').val($(this).find('.card-subtitle').html());
                    $('input[name="rno"]').val(rno);

                    $(".modal-footer .btn").hide();
                    $(".replyRemove, .replyModify, .replyClose").show();

                    modal.modal('show');
                }); // replyList()

                <!-- 댓글 삭제 -->
                $(".replyRemove").on("click", function() {
                    var rno = $("input[name='rno']").val(); // hidden으로 처리된 모달창의 댓글 번호

                    $.ajax({
                        url: '/replies/' + rno,
                        method: 'delete',
                        success: function(result) {
                            console.log("result = " + result);
                            if(result === 'success') {
                                alert("댓글이 삭제되었습니다.");
                                modal.modal('hide');
                                loadJSONData();
                            }
                        } // success
                    }) // ajax
                }); // replyRemove()

                <!-- 댓글 수정 -->
                $(".replyModify").on("click", function() {
                    var rno = $("input[name='rno']").val(); // hidden으로 처리된 모달창의 댓글 번호

                    var reply = {
                        rno: rno,
                        bno: bno,
                        text: $('input[name="replyText"]').val(),
                        replyer: $('input[name="replyer"]').val()
                    }
                    console.log(reply);

                    $.ajax({
                        url: '/replies/' + rno,
                        method: 'put',
                        data: JSON.stringify(reply),
                        contentType: 'application/json; charset=utf-8',

                        success: function(result) {
                            console.log("result = " + result);

                            if(result === 'success') {
                                alert("댓글이 수정되었습니다.");
                                modal.modal('hide');
                                loadJSONData();
                            }
                        } // success
                    }) // ajax
                }); // replyModify()

            }); // end script
