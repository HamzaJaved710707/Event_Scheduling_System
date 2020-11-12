var progressBar = document.getElementById("progress_id");
$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip();
});
window.onload = function() {
    console.log(localStorage["currentUserId"]);
    loadNumbers();
    ratedUser();

};
// Function to redirect user to user detail page
function GoToAllUsers() {
    window.location = "user_detail.html";
}
// Function to redirect user to service provider detail
function GoToServiceProvider() {
    window.location = "service_provider_detail.html";

}
// Function to redirect user to client user_detail
function GoToClients() {
    window.location = "client_detail.html";
}
// 
function loadNumbers() {
    var totalUsers = 0;
    var organizers = 0;
    var clients = 0;
    firebase.firestore().collection("Users").get()
        .then(function(querySnapshot) {
            querySnapshot.forEach(function(doc) {
                var value = doc.data();
                if (value.type == 0) {
                    organizers++;
                } else if (value.type == 1) {
                    clients++;
                }
                totalUsers++;
            });
            progressBar.style.display = "none";
            document.getElementById("users_number").innerText = totalUsers - 1;
            document.getElementById("organizer_number").innerText = organizers;
            document.getElementById("client_numbers ").innerText = clients;
        })
        .catch(function(error) {
            console.log("Error getting documents: ", error);
        });

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

function ratedUser() {
    var myvar = "";
    var ratedUserId = document.getElementById('rated_id');
    var next = firebase.firestore().collection("Users")
        .orderBy("ratingStar", "desc");
    next.get()
        .then(function(querySnapshot) {
            var counter = 0;
            querySnapshot.forEach(function(doc) {
                    counter++;
                    // doc.data() is never undefined for query doc snapshots
                    myvar += '<tr>' +
                        '                                        <th>' +
                        counter + '</th>' +
                        '                                        <td>' +
                        doc.data().Name + '</td>' +
                        '                                        <td>' +
                        doc.data().ratingStar +
                        '</td>' +
                        '                                     <td  data-userid=' + doc.data().id + '  onClick= "LayoutClick(this)"><button type="button" class="btn btn-success btn-sm">View Profile</button></td>' +
                        '                                        <td  data-userid=' + doc.data().id + '  onClick= "MessageBtn(this)"><button type="button" class="btn btn-info btn-sm">Message</button></td>' +
                        '                                     <td ><button type="button" class="btn btn-danger btn-sm" data-userid=' + doc.data().id + ' data-status= ' + doc.data().isActive + '  onClick= "DeleteUser(this)">Block</button></td>' +
                        '                                    </tr>';


                }


            );
            ratedUserId.innerHTML = myvar;
        })
        .catch(function(error) {
            console.log("Error getting documents: ", error);
        });



} // Change the status of user from IsActive to false
function DeleteUser(uid) {





    var data = uid.getAttribute("data-userid");
    var activeStatus = uid.getAttribute("data-status");

    if (activeStatus == "true") {

        cuteAlert({
            type: "question",
            title: "Alert",
            message: "Do you want to block this user?",
            confirmText: "Confirm!",
            cancelText: "Cancel!",

        }).then((e) => {
            if (e == "confirm") {

                var db = firebase.firestore();
                var UserRef = db.collection("Users").doc(data);
                return UserRef.update({
                        isActive: false
                    })
                    .then(function() {
                        uid.innerText = "Unblock";
                        uid.style.background = "green";
                        console.log("Document successfully updated!");
                        uid.setAttribute("data-status", "false");
                    })
                    .catch(function(error) {
                        // The document probably doesn't exist.
                        console.error("Error updating document: ", error);
                    });

            } else {

            }
        });


    } else if (activeStatus == "false") {

        var db = firebase.firestore();
        var UserRef = db.collection("Users").doc(data);
        return UserRef.update({
                isActive: true
            })
            .then(function() {
                uid.innerText = "Block";
                uid.style.background = "red";
                console.log("Document successfully updated!");
                uid.setAttribute("data-status", "true");

            })
            .catch(function(error) {
                // The document probably doesn't exist.
                console.error("Error updating document: ", error);
            });
    }


} // Open to inbox.html
function MessageBtn(uid) {
    var data = uid.getAttribute("data-userid");
    localStorage["chatUserId"] = data;
    window.location = "inbox.html";
}

function LayoutClick(uid) {
    var data = uid.getAttribute("data-userid");
    localStorage["chatUserId"] = data;
    window.location = "user_profile_detail.html";
}