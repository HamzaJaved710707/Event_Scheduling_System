var progressBar = document.getElementById("progress_id");
$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip();
});
window.onload = function() {
    console.log(localStorage["currentUserId"]);
    loadNumbers();

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