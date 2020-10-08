const functions = require('firebase-functions');
const admin = require("firebase-admin");
admin.initializeApp()

exports.sayHello = functions.https.onCall ((data, context) => {
  const text = data.text;
  var db = admin.firestore();
  const webDetection = JSON.parse('{"webEntities": [{"entityId": "/m/02p7_j8","score": 1.44225,"description": "Carnival in Rio de Janeiro"},{"entityId": "/m/06gmr","score": 1.2913725,"description": "Rio de Janeiro"}]}');
        let imageRef = db.collection('images').doc(text);
        imageRef.set(webDetection);
});

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
