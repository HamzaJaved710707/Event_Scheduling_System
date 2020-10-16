var progressBar;

$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip();
});

window.onload = function() {


    progressBar = document.getElementById("progress_id");
    var db = firebase.firestore();

    var exRef = db.collection('Users');
    var allex = exRef
        .get()
        .then(snapshot => {
            var val;
            var htmlValue = '';
            snapshot.forEach(doc => {
                val = doc.data();

                if (Number(val.type) == Number(0)) {
                    htmlValue += '<tr>';
                    htmlValue += '<th>' + val.id + '</th>';
                    htmlValue += '<td>' + val.Name + '</td>';
                    htmlValue += '<td>' + 'Service Provider' + '</td>';
                    htmlValue += ' <td ><button type="button" class="btn btn-info btn-sm"  data-userid=' + val.id + '  onClick= "MessageBtn(this)">Message</button></td>';
                    htmlValue += '<td ><button type="button" class="btn btn-danger btn-sm" data-userid=' + val.id + ' data-status= ' + val.isActive + '  onClick= "DeleteUser(this)">Delete</button></td>';
                    htmlValue += '</tr>';
                } else if (Number(val.type) == Number(1)) {
                    htmlValue += '<tr>';
                    htmlValue += '<th>' + val.id + '</th>';
                    htmlValue += '<td>' + val.Name + '</td>';
                    htmlValue += '<td>' + 'Client' + '</td>';
                    htmlValue += ' <td ><button type="button" class="btn btn-info btn-sm"  data-userid=' + val.id + '  onClick= "MessageBtn(this)">Message</button></td>';
                    htmlValue += '<td ><button type="button" class="btn btn-danger btn-sm" data-userid=' + val.id + ' data-status= ' + val.isActive + '  onClick= "DeleteUser(this)">Delete</button></td>';
                    htmlValue += '</tr>';

                } else {
                    return;
                }

            });
            progressBar.style.display = "none";

            document.getElementById("user_table_body").innerHTML = htmlValue;



        })
        .catch(err => {
            console.log('Error getting documents', err);
        });
}; // Open to inbox.html
function MessageBtn(uid) {
    var data = uid.getAttribute("data-userid");
    localStorage["chatUserId"] = data;
    window.location = "inbox.html";
}

// Change the status of user from IsActive to false
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
                        uid.innerText = "UnBlock";
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
                uid.innerText = "Delete";
                uid.style.background = "red";
                console.log("Document successfully updated!");
                uid.setAttribute("data-status", "true");

            })
            .catch(function(error) {
                // The document probably doesn't exist.
                console.error("Error updating document: ", error);
            });
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