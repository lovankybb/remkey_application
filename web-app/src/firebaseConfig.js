// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { sendNotificationTokenToServer } from "./service/FireBaseService";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

const API_KEY = import.meta.env.VITE_API_KEY;
const AUTH_DOMAIN = import.meta.env.VITE_AUTH_DOMAIN;
const PROJECT_ID = import.meta.env.VITE_PROJECT_ID;
const STORAGE_BUCKET = import.meta.env.VITE_STORAGE_BUCKET;
const MESSAGING_SENDER_ID = import.meta.env.VITE_MESSAGING_SENDER_ID;
const APP_ID = import.meta.env.VITE_APP_ID;
const MEASUREMENT_ID = import.meta.env.VITE_MEASUREMENT_ID;

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: API_KEY,
  authDomain: AUTH_DOMAIN,
  projectId: PROJECT_ID,
  storageBucket: STORAGE_BUCKET,
  messagingSenderId: MESSAGING_SENDER_ID,
  appId: APP_ID,
  measurementId: MEASUREMENT_ID,
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

const messaging = getMessaging(app);

export const requestForToken = async () => {
  try {
    console.log("checking service worker");
    const registration = await navigator.serviceWorker.register(
      "/firebase-messaging-sw.js",
    );
    await navigator.serviceWorker.ready;
    console.log("Service worker ready to scope: ", registration.scope);

    const currentToken = await getToken(messaging, {
      vapidKey:
        "BJpiUo0LXr_dVem3WdSReYsVsLxRMPoSSmvw1qTRxg3qWjycaSXpCtqwMgyXjFTikd22iWacreHhJ_7rCUvsF98",
      serviceWorkerRegistration: registration,
    });

    if (currentToken) {
      console.log("Current token for client: ", currentToken);

      const res = await sendNotificationTokenToServer(currentToken);
      return res;
    }
  } catch (err) {
    if (err.code === "messaging/permission-blocked") {
      console.error("Bạn đã chặn quyền thông báo trên trình duyệt!");
    }
    console.log("An error occurred while retrieving token: ", err);
    throw err;
  }
};

export const onMessageListener = () =>
  new Promise((resolve, reject) => {
    if (!messaging) return reject("Messaging not initialized");

    onMessage(messaging, (payload) => {
      if (Notification.permission === "granted") {
        new Notification(payload.notification.title, {
          body: payload.notification.body,
          icon: payload.notification.image || "./logo.png",
        });
      }
    });
  });
