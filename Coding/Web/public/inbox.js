var chatUserId = localStorage["chatUserId"];
var currentUserId = localStorage["currentUserId"];
var chat_Collection_Reference = firebase.firestore().collection("chats");
var conversation_Collection_Reference = firebase.firestore().collection("conversation");
var LoadCurrentUserMsg;
var LoadChatUserMsg;
var LoadRecentCOntactListener;
var div_value = "";
var contact_value = "";
var current_div;
console.log(currentUserId);
console.log(chatUserId);
var userCollection = firebase.firestore().collection("Users");
var progressBar;


function getUserCollection() {
    firebase.firestore().collection("Users")
        .get()
        .then(function(querySnapshot) {
            querySnapshot.forEach(function(doc) {
                // doc.data() is never undefined for query doc snapshots
                console.log(doc.id, " => ", doc.data());
            });
        })
        .catch(function(error) {
            console.log("Error getting documents: ", error);
        });
}
// send Message Method

function sendMessage() {
    var value = 1;
    var msg_data = document.getElementById("type_msg_editText").value;
    msg_data = msg_data.trim();
    if (msg_data === "") {
        document.getElementById("type_msg_editText").value = "";


    } else {
        var chatdoc = userCollection.doc(chatUserId);
        chatdoc.get().then(function(doc1) {
            var chatUserName;
            var currentUserName;
            // IF user exists then store its data
            if (doc1.exists) {
                document.getElementById("type_msg_editText").value = "";
                var currentdoc = userCollection.doc(currentUserId);
                currentdoc.get().then(function(doc2) {
                    currentUserName = doc2.data().Name;
                    var val = doc1.data();
                    chatUserName = val.Name;
                    if (value == 1) {

                        var docData = {
                            Name: chatUserName,
                            Id: chatUserId,
                            timeStamp: new Date().getTime()
                        };

                        var docData3 = {
                            Name: currentUserName,
                            Id: currentUserId,
                            timeStamp: new Date().getTime()
                        };
                        // run query
                        userCollection.doc(currentUserId).collection("conversation").doc(chatUserId).set(docData);
                        userCollection.doc(chatUserId).collection("conversation").doc(currentUserId).set(docData3);

                    }
                    var docData2 = {
                        message: msg_data,
                        seen: false,
                        from: chatUserId,
                        timeStamp: new Date().getTime()
                    };
                    chat_Collection_Reference.doc(currentUserId).collection(chatUserId).doc().set(docData2);
                    chat_Collection_Reference.doc(chatUserId).collection(currentUserId).doc().set(docData2);
                }).catch(function(error) {
                    console.log("Error getting document:", error);
                });


            } else {
                // doc.data() will be undefined in this case
            }
        }).catch(function(error) {
            console.log("Error getting document:", error);
        });

    }
}


// Load Alreading existing messages in Firestore
window.onload = function() {
    LoadMessages();
    LoadRecentContact();
    progressBar = document.getElementById("progress_id");
};
//Load recent contact 
function LoadRecentContact() {
    LoadRecentCOntactListener = userCollection.doc(currentUserId).collection("conversation").onSnapshot(function(snapshot) {
        snapshot.docChanges().forEach(function(change) {
            if (change.type === "added") {
                var value = change.doc.data();

                var dateValue = value.timeStamp;
                var date = new Date(dateValue);
                date = date.toLocaleDateString();
                contact_value += '<div class="chat_list" data-chatID= ' + value.Id + ' onClick= "setID(this)">' +
                    '                            <div class="chat_people">' +
                    '                                <div class="chat_img"> <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>' +
                    '                                <div class="chat_ib">' +
                    '                                    <h5 style = "color: orange;">' + value.Name + '<span class="chat_date" style = "color: orange;">' + date + '</span></h5>' +
                    '                                </div>' +
                    '                            </div>' +
                    '                        </div>';
            }


            if (change.type === "modified") {
                console.log("Modified city: ", change.doc.data());
            }
            if (change.type === "removed") {
                console.log("Removed city: ", change.doc.data());
            }
        });
        var alreadyData = document.getElementById("contact_inbox_chat");
        alreadyData.innerHTML += contact_value;
        contact_value = "";
        progressBar.style.display = "none";
    });


}

function setID(chatId) {
    if (current_div != undefined) {
        current_div.style.background = "white";
    }

    current_div = chatId;
    var alreadyData = document.getElementById("id_msg_history");
    alreadyData.innerHTML = "";
    chatUserId = chatId.getAttribute("data-chatID");
    current_div.style.background = "black";
    if (LoadCurrentUserMsg != undefined) {
        LoadCurrentUserMsg();

    }
    LoadMessages();


}
// Load the recent messages of Given Client
function LoadMessages() {
    if (chatUserId != undefined) {
        LoadCurrentUserMsg = chat_Collection_Reference.doc(currentUserId).collection(chatUserId).orderBy("timeStamp")
            .onSnapshot(function(snapshot) {
                snapshot.docChanges().forEach(function(change) {
                    if (change.type === "added") {
                        if (change.doc.data().from == chatUserId) {
                            div_value += '<div class="outgoing_msg">' +
                                '                            <div class="sent_msg">' +
                                '                                <p>' + change.doc.data().message +
                                '                                </p>' +
                                '                                <span class="time_date"> 11:01 AM    |    June 9</span> </div>' +
                                '                        </div>';
                            var alreadyData = document.getElementById("id_msg_history");
                            alreadyData.innerHTML += div_value;
                            div_value = "";

                        }
                        if (change.doc.data().from == currentUserId) {
                            div_value += '<div class="incoming_msg">' +
                                '                            <div class="incoming_msg_img"> <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>' +
                                '                            <div class="received_msg">' +
                                '                                <div class="received_withd_msg">' +
                                '                                    <p>' + change.doc.data().message +
                                '                                    </p>' +
                                '                                    <span class="time_date"> 11:01 AM    |    June 9</span></div>' +
                                '                            </div>' +
                                '                        </div>';


                            var alreadyData = document.getElementById("id_msg_history");
                            alreadyData.innerHTML += div_value;
                            div_value = "";
                        }

                    }
                    if (change.type === "modified") {
                        console.log("Modified city: ", change.doc.data());
                    }
                    if (change.type === "removed") {
                        console.log("Removed city: ", change.doc.data());
                    }
                });
            });
    }


}



window.onbeforeunload = function() {
    if (LoadCurrentUserMsg != null) {
        LoadCurrentUserMsg();

    }
}

function logOut() {

    cuteAlert({
        type: "question",
        title: "Alert",
        message: "Do you want to log out?",
        confirmText: "Confirm!",
        cancelText: "Cancel!",

    }).then((e) => {
        if (e == "confirm") {

            firebase.auth().signOut().then(function() {
                window.location = "index.html";
            }).catch(function(error) {});

        } else {

        }
    });

}