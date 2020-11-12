$(document).ready(function() {
    $('[data-toggle="tooltip"]').tooltip();
});
var chatUserId = localStorage["chatUserId"];
var progressBar;
window.onload = function() {
    progressBar = document.getElementById("progress_id");
    var layoutID = document.getElementById("home");
    var nameId = document.getElementById("main_name");
    var imageSrc = document.getElementById("blah");
    var db = firebase.firestore();
    var UserRef = db.collection("Users").doc(chatUserId);
    UserRef.get().then(function(doc1) {
        var chatUserName;
        var currentUserName;
        // IF user exists then store its data
        if (doc1.exists) {
            var dataValue = doc1.data();
            var status = dataValue.isActive;
            var statusValue;
            var catStatus;
            if (status == true) {
                statusValue = "User's account is active";
            } else {
                statusValue = "User's account is blocked";
            }
            catStatus = dataValue.businessCat;
            if (catStatus == undefined) {
                catStatus = "User";
            }
            var ratingStars = dataValue.ratingStar;
            var myvar = '<div class="row">' + ' <div class="col-md-6">' + ' <label>Name</label>' + '</div>' +
                '<div class="col-md-6">' + ' <p>' +
                dataValue.Name + '</p>' + '</div>' + '</div>' + '<div class="row">' +
                ' <div class="col-md-6">' + '<label>Email</label>' +
                '</div>' + '<div class="col-md-6">' + '<p>' + dataValue.email +
                '</p>' + '</div>' + '</div>' + ' <div class="row">' +
                '<div class="col-md-6">' +
                '                                    <label>Phone</label>' +
                '                                </div>' +
                '                                <div class="col-md-6">' +
                '                                    <p>' + dataValue.mobileNumber +
                '</p>' +
                '                                </div>' +
                '                            </div>' +
                '' +
                '                            <div class="row">' +
                '                                <div class="col-md-6">' +
                '                                    <label>Active Status</label>' +
                '                                </div>' +
                '                                <div class="col-md-6">' +
                '                                    <p>' + statusValue +
                '</p>' +
                '                                </div>' +
                '                            </div>' +
                '                            <div class="row">' +
                '                                <div class="col-md-6">' +
                '                                    <label>Category</label>' +
                '                                </div>' +
                '                                <div class="col-md-6">' +
                '                                    <p>' + catStatus +
                '</p>' +
                '                                </div>' +
                '                           </div>';
            if (ratingStars != undefined) {
                if (ratingStars === 1) {
                    myvar += '                            <div class="row">' +
                        '                                <div class="col-md-6">' +
                        '                                    <label>Rating</label>' +
                        '                                </div>' +
                        '                                <div class="col-md-6">' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                </div>' +
                        '                            </div>';

                } else if (ratingStars === 2) {
                    myvar += '                            <div class="row">' +
                        '                                <div class="col-md-6">' +
                        '                                    <label>Rating</label>' +
                        '                                </div>' +
                        '                                <div class="col-md-6">' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                </div>' +
                        '                            </div>';

                } else if (ratingStars === 3) {
                    myvar += '                            <div class="row">' +
                        '                                <div class="col-md-6">' +
                        '                                    <label>Rating</label>' +
                        '                                </div>' +
                        '                                <div class="col-md-6">' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                </div>' +
                        '                            </div>';
                } else if (ratingStars === 4) {
                    myvar += '                            <div class="row">' +
                        '                                <div class="col-md-6">' +
                        '                                    <label>Rating</label>' +
                        '                                </div>' +
                        '                                <div class="col-md-6">' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                </div>' +
                        '                            </div>';
                } else if (ratingStars === 5) {
                    myvar += '                            <div class="row">' +
                        '                                <div class="col-md-6">' +
                        '                                    <label>Rating</label>' +
                        '                                </div>' +
                        '                                <div class="col-md-6">' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star checked"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                    <span class="fa fa-star"></span>' +
                        '                                </div>' +
                        '                            </div>';
                }
            } else {
                myvar += '                            <div class="row">' +
                    '                                <div class="col-md-6">' +
                    '                                    <label>Rating</label>' +
                    '                                </div>' +
                    '                                <div class="col-md-6">' +
                    '                                    <span class="fa fa-star "></span>' +
                    '                                    <span class="fa fa-star "></span>' +
                    '                                    <span class="fa fa-star "></span>' +
                    '                                    <span class="fa fa-star"></span>' +
                    '                                    <span class="fa fa-star"></span>' +
                    '                                </div>' +
                    '                            </div>';

            }

            layoutID.innerHTML = myvar;
            nameId.innerHTML = dataValue.Name;
            imageSrc.src = dataValue.imgUrl;
        } else {
            // doc.data() will be undefined in this case
        }
    }).catch(function(error) {
        console.log("Error getting document:", error);
    });


};

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