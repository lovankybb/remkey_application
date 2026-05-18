import { useEffect, useRef, useState } from "react";
import Header from "../components/Header/Header";
import MainContent from "../components/MainContent/MainContent";
import { onMessageListener, requestForToken } from "../firebaseConfig";

export default function Home() {
  const [enableNotificationRedDot, setEnableNotificationRedDot] = useState(true);

  const initialized = useRef(false);

  useEffect(() => {

    if (!localStorage.getItem("jwtToken")) window.location.href = "/login";

    if (!initialized.current) {
      initialized.current = true;
      const setupFCM = async () => {
        try {
          await requestForToken();
          const payload = await onMessageListener();
          if (payload) {
            setEnableNotificationRedDot(true);
          }
          console.log("Message received", payload);
        } catch (err) {
          console.log("FCM Setup err: ", err);
        }
      };
      setupFCM();
    }
  }, []);

  const [selectedContent, setSelectedContent] = useState("home-content-tab");

  const headerEvents = {
    openAccountTab: () => {
      setSelectedContent("account-tab");
    },
    openHomeContentTab: () => {
      setSelectedContent("home-content-tab");
    },
    openMyCardTab: () => {
      setSelectedContent("mycard-tab");
    },
    openServicePackageTab: () => {
      setSelectedContent("service-package-tab");
    },
  };

  return (
    <>
      <Header
        {...headerEvents}
        selectedNavigation={selectedContent}
        setEnableNotificationRedDot={setEnableNotificationRedDot}
        enableNotificationRedDot={enableNotificationRedDot}
      />
      <MainContent selectedContent={selectedContent} />
    </>
  );
}
