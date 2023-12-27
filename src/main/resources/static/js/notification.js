function formatDateTime(rawDate) {
        const formattedDate = moment(rawDate, 'YYYY-MM-DDTHH:mm:ss').format('YYYY.MM.DD HH:mm');
        return formattedDate;
    }

   // 서버에서 알림 데이터를 가져와 동적으로 렌더링하는 함수
    function loadNotifications() {
        $.ajax({
            url: "/boardnotification/read", // 알림 데이터를 제공하는 엔드포인트
            method: "GET",
            dataType: "json",
            success: function(data) {
                // 알림 데이터를 동적으로 렌더링합니다.
                const notificationContainer = $("#notification-container");

                data.forEach(function(notification) {
                    // 알림을 동적으로 생성하여 추가합니다.
                    const notificationElement = `
                        <div class="notification">
                            <i class="fas fa-bell"></i>
                            <div class="nickname">${notification.replynickname}<span>님이 댓글을 남겼습니다.</span></div>
                            <a href="/read/${notification.bid}">
                                <div>${notification.message}</div>
                            </a>
                            <div class="date">${formatDateTime(notification.rdate)}</div>
                        </div>
                    `;
                    notificationContainer.append(notificationElement);
                });
            },
            error: function(error) {
                console.error("Error fetching notifications: " + error);
            }
        });
    }

    // 페이지가 로드될 때 알림을 가져와서 렌더링합니다.
    $(document).ready(function() {
        loadNotifications();
    });

    //---------------------------------
        //소켓 연결
        //---------------------------------
        var socket = new WebSocket('ws://localhost:8080/socket');

        //---------------------------------
        //메세지 수신
        //---------------------------------
        let amarmtest = 0;
        socket.onmessage = function(event) {
                var message = event.data;
                console.log('message : ',message);
                alert("Message : "+message);

                const notificationSpan = document.querySelector('.notification .counter');
                if (notificationSpan) {
                  notificationSpan.style.display = 'block';
                }
       };