const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

// // Create and deploy your first functions
// // https://firebase.google.com/docs/functions/get-started
//
exports.helloWorld = functions.https.onCall(() => {
  functions.logger.info("Hello logs!", { structuredData: true });
  response.send({
    message: "Hello from Firebase!",
  });
});

// update user biodata, if doesn't exit create one
exports.updateUserBiodata = functions.https.onCall((data) => {
  const { uid, name, age, weight, height } = data;
  const db = admin.firestore();
  const userRef = db.collection("biodata").doc(uid);
  return userRef
    .get() // get user biodata
    .then((doc) => {
      if (doc.exists) {
        // if user biodata exist, update it
        return userRef.update({ name, age, weight, height });
      } else {
        // if user biodata doesn't exist, create one
        return userRef.set({ name, age, weight, height });
      }
    }
    )
    .then(() => {
      return {
        message: "User biodata updated successfully",
        name,
        age,
        weight,
        height
      };
    }
    )
    .catch((error) => {
      return { error };
    }
    );
});

// get user biodata by uid
exports.getUserBiodata = functions.https.onCall((data) => {
  const { uid } = data;
  const db = admin.firestore();
  const userRef = db.collection("biodata").doc(uid);
  return userRef
    .get()
    .then((doc) => {
      if (doc.exists) {
        return doc.data();
      } else {
        return { message: "User biodata not found" };
      }
    }
    )
    .catch((error) => {
      return { error };
    }
    );
});

// add user bmi to collectiontion history > bmi > user id collection
exports.addUserBmi = functions.https.onCall((data) => {
  const { uid, height, weight } = data;
  // calculate bmi
  const calculated = (weight / (height * height)) * 10000;
  const timestamp = admin.firestore.FieldValue.serverTimestamp();
  const db = admin.firestore();
  const userRef = db.collection("history").doc("bmi").collection(uid);
  return userRef
    .add({ height, weight, calculated, created_at: timestamp })
    .then(() => {
      return {
        message: "User bmi added successfully",
        bmi: calculated
      };
    })
    .catch((error) => {
      return { error };
    }
    );
});

// get user bmi history by uid

exports.getUserBmiHistory = functions.https.onCall((data) => {
  const { uid } = data;
  const db = admin.firestore();
  const userRef = db.collection("history").doc("bmi").collection(uid);
  return userRef
    .get()
    .then((snapshot) => {
      if (snapshot.empty) {
        return { message: "User bmi history not found" };
      } else {
        const data = [];
        snapshot.forEach((doc) => {
          data.push(doc.data());
        });
        return data;
      }
    })
    .catch((error) => {
      return { error };
    });
});

// add user blood pressure reading to collectiontion history > blood pressure > user id collection
exports.addUserBloodPressure = functions.https.onCall((data) => {
  const { uid, sys, dia, bp } = data;
  const timestamp = admin.firestore.FieldValue.serverTimestamp();
  const db = admin.firestore();
  const userRef = db.collection("history").doc("blood_pressure").collection(uid);
  return userRef
    .add({ sys, dia, bp, created_at: timestamp })
    .then(() => {
      return {
        message: "User blood pressure added successfully",
      };
    })
    .catch((error) => {
      return { error };
    }
    );
});

// get user blood pressure

exports.getUserBloodPressureHistory = functions.https.onCall((data) => {
  const { uid } = data;
  const db = admin.firestore();
  const userRef = db.collection("history").doc("blood_pressure").collection(uid);
  return userRef
    .get()
    .then((snapshot) => {
      if (snapshot.empty) {
        return { message: "User blood pressure history not found" };
      } else {
        const data = [];
        snapshot.forEach((doc) => {
          data.push(doc.data());
        });
        return data;
      }
    })
    .catch((error) => {
      return { error };
    });
});

exports.getDisease = functions.https.onCall((data) => {
  const { symptoms } = data;

  // get the score for COVID19
  function getCovid19Score(symptoms) {
    let score = 0;
    if (symptoms.includes('Fever or chills')) {
      score++;
    }
    if (symptoms.includes('Continuous cough')) {
      score++;
    }
    if (symptoms.includes('Fatigue')) {
      score++;
    }
    if (symptoms.includes('Loss of taste or smell')) {
      score++;
    }
    if (symptoms.includes('Shortness of breath or difficulty breathing')) {
      score++;
    }
    if (symptoms.includes('Sore throat')) {
      score++;
    }
    return score;
  }

  // get the score for TB
  function getTbScore(symptoms) {
    let score = 0;
    if (symptoms.includes('Persistent cough (last more than 2 weeks)')) {
      score++;
    }
    if (symptoms.includes('Cough with blood in sputum')) {
      score++;
    }
    if (symptoms.includes('Fever for more than 2 weeks')) {
      score++;
    }
    if (symptoms.includes('Pain in chest')) {
      score++;
    }
    if (symptoms.includes('Weight loss')) {
      score++;
    }
    if (symptoms.includes('Loss of appetite')) {
      score++;
    }
    return score;
  }

  // get the score for DENGUE
  function getDengueScore(symptoms) {
    let score = 0;
    if (symptoms.includes('Sudden onset very high fever')) {
      score++;
    }
    if (symptoms.includes('Severe headache')) {
      score++;
    }
    if (symptoms.includes('Muscles and joints become painful')) {
      score++;
    }
    if (symptoms.includes('Skin rashes')) {
      score++;
    }
    if (symptoms.includes('Fatigue')) {
      score++;
    }
    if (symptoms.includes('Vomiting')) {
      score++;
    }
    return score;
  }

  if (symptoms.length > 0) {
    // get the disease with the highest score
    let disease = {
      name: '',
      score: 0
    };

    // get the score for each disease
    let covid19Score = getCovid19Score(symptoms);
    let tbScore = getTbScore(symptoms);
    let dengueScore = getDengueScore(symptoms);

    // get the disease with the highest score
    if (covid19Score > disease.score) {
      disease.name = 'COVID19';
      disease.score = covid19Score;
    }
    if (tbScore > disease.score) {
      disease.name = 'TB';
      disease.score = tbScore;
    }
    if (dengueScore > disease.score) {
      disease.name = 'DENGUE';
      disease.score = dengueScore;
    }

    return {
      disease: disease.name,
      score: disease.score,
      status: 'success',
      message: 'Disease found'
    };
  } else {
    return {
      status: 'error',
      message: 'No symptoms selected'
    };
  }


});
