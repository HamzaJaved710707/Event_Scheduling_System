PDFObject.embed('Documentation.pdf', "#example1");

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