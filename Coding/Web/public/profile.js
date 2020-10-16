$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip();
});
var image_data;
var currentUserId;
var storage = firebase.storage();
var storageRef = storage.ref();
var imagesRef = storageRef.child('Admin/Images');
const reader = new FileReader();
const fileInput = document.getElementById("imgInp");
const img = document.getElementById("blah");
let file;
var imageUrl1;
var imageUrl2;
var blob;
reader.onload = e => {
    img.src = e.target.result;
    imageUrl1 = e.target.result;
    blob = new Blob([e.target.result], { type: "image/jpeg" });
    var uploadTask = imagesRef.child('1').put(blob);
    uploadTask.on('state_changed', function(snapshot) {
        // Observe state change events such as progress, pause, and resume
        // Get task progress, including the number of bytes uploaded and the total number of bytes to be uploaded
        var progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
        console.log('Upload is ' + progress + '% done');
        switch (snapshot.state) {
            case firebase.storage.TaskState.PAUSED: // or 'paused'
                console.log('Upload is paused');
                break;
            case firebase.storage.TaskState.RUNNING: // or 'running'
                console.log('Upload is running');
                break;
        }
    }, function(error) {
        console.log(error);
        // Handle unsuccessful uploads
    }, function() {
        // Handle successful uploads on complete
        // For instance, get the download URL: https://firebasestorage.googleapis.com/...
        uploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {

            console.log('File available at', downloadURL);

            var db = firebase.firestore().collection("Users").doc(currentUserId);
            return db.update({
                    imgUrl: downloadURL
                })
                .then(function() {
                    console.log("File uploaded successfully ")
                })
                .catch(function(error) {
                    // The document probably doesn't exist.
                    console.error("Error updating document: ", error);
                });
        });
    });





}

fileInput.addEventListener('change', e => {
    const f = e.target.files[0];
    file = f;
    imageUrl2 = reader.readAsDataURL(f);
    console.log(file);

})



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