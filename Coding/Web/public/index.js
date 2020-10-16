var progressBar;

window.onload = function() {
    progressBar = document.getElementById("progress_id");

    progressBar.style.display = "none";
}

function test() {

    progressBar.style.display = "display";
    var email = document.getElementById('txt_Email').value;
    var password = document.getElementById('txt_Password').value;
    if (!(email.trim() == "") && !(password.trim() == "")) {

        firebase.auth().signInWithEmailAndPassword(email, password).then(user => {
            progressBar.style.display = "none";
            window.location = "dashboard.html";
        }).catch(function(error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            console.log(errorCode);
            console.log(errorMessage);
            cuteAlert({
                type: "error",
                title: "Error",
                message: error,
                buttonText: "Try again",
            });
            // ...
        });
    } else {

        cuteAlert({
            type: "warning",
            title: "Empty!",
            message: "Please fill the fields first",
            buttonText: "Ok",
        });
    }
}

firebase.auth().onAuthStateChanged(function(user) {
    if (user) {
        console.log(user.uid);
        localStorage["currentUserId"] = user.uid;


    } else {
        // No user is signed in.
    }
});