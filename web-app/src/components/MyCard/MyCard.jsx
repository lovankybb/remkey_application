import { useState } from "react";
import CardHeader from "./CardHeader";
import CreateCard from "./CreateCard";
import Topic from "./Topic";
import Language from "./Language";
import MyCardManagement from "./MyCardManagement";
import StudyCards from "./StudyCard";
import RateButtons from "./RatingBtns";
import ErrorPopup from "../Popup/ErrorPopup";
import { updateNextReviewTime } from "../../service/CardUserService";
import RatingBtns from "./RatingBtns";
import DisableLayer from "../Popup/DisableLayer";
import ReportManagement from "./ReportManagement";

function MyCard() {


  const [isCloseTab, setIsCloseTab] = useState(false);
  const [enableCreateCard, setEnableCreateCard] = useState(false);
  const [enableTopicTab, setEnableTopicTab] = useState(false);
  const [enableLanguageTab, setEnableLanguageTab] = useState(false);
  const [enableMyCardManagement, setEnableMyCardManagement] = useState(false);
  const [enableReportManagement, setEnableReportManagement] = useState(false); 
  const [errMessage, setErrMessage] = useState("");
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);

  const openCardOptions = {
    openCreateCard: () => {
      setEnableCreateCard(true);
    },
    openTopicTab: () => {
      setEnableTopicTab(true);
    },
    openLanguageTab: () => {
      setEnableLanguageTab(true);
    },
    openMyCardManagementTab: () => {
      setEnableMyCardManagement(true);
    },
    openReportTab: () => {
      setEnableReportManagement(true); 
    }
  };

  const closeCreateCardTab = () => {
    setIsCloseTab(true);

    setTimeout(() => {
      setEnableCreateCard(false);
      setIsCloseTab(false);
    }, 150);
  };

  const closeTopicTab = () => {
    setIsCloseTab(true);

    setTimeout(() => {
      setEnableTopicTab(false);
      setIsCloseTab(false);
    }, 150);
  };

  const closeLanguageTab = () => {
    setIsCloseTab(true);

    setTimeout(() => {
      setEnableLanguageTab(false);
      setIsCloseTab(false);
    }, 150);
  };

  const closeMyCardManagementTab = () => {
    setIsCloseTab(true);

    setTimeout(() => {
      setEnableMyCardManagement(false);
      setIsCloseTab(false);
    }, 150);
  };


  const closeReportManagementTab = () => {
    setIsCloseTab(true);

    setTimeout(() => {
      setEnableReportManagement(false);
      setIsCloseTab(false);
    }, 150);
  };

  const [index, setIndex] = useState(0);
  const [enableRatingBtn, setEnableRatingBtn] = useState(true);
  const [cards, setCards] = useState([]);

  function moveToNextCard() {
    setIsCloseTab(true);
    setTimeout(() => {
      setIndex((prev) => prev + 1);
      setIsCloseTab(false);
    }, 200);
  }

  async function handleUpdateNextReviewTime(rating) {
    const cardId = cards[index].id;

    const data = await updateNextReviewTime(cardId, rating);
    if (data.code !== 1000) {
      const message = `code: ${data.code}
                      message: ${data.message}`;
      setErrMessage(message);
      setEnableDisableLayer(true);
      setEnableErrorPopup(true);
    }
  }

  function closeErrorPopup() {
    setEnableErrorPopup(false);
    setEnableDisableLayer(false);
  }

  return (
    <>
      <div className="mycard-container">
        <CardHeader openCardOptions={openCardOptions} />
        <hr />
        {enableCreateCard && (
          <CreateCard exitBtn={closeCreateCardTab} isClose={isCloseTab} />
        )}
        {enableTopicTab && (
          <Topic exitBtn={closeTopicTab} isClose={isCloseTab} />
        )}

        {enableLanguageTab && (
          <Language exitBtn={closeLanguageTab} isClose={isCloseTab} />
        )}

        {enableMyCardManagement && (
          <MyCardManagement
            exitBtn={closeMyCardManagementTab}
            isClose={isCloseTab}
          />
        )}
        {enableReportManagement && <ReportManagement isClose={isCloseTab} exitBtn={closeReportManagementTab}/>}
        <StudyCards
          cards={cards}
          setCards={setCards}
          isClose={isCloseTab}
          index={index}
          setEnableRatingBtn={setEnableRatingBtn}
        />
        {enableRatingBtn && (
          <RatingBtns
            updateNextReviewTime={handleUpdateNextReviewTime}
            moveToNextCard={moveToNextCard}
          />
        )}
      </div>
      {enableDisableLayer && <DisableLayer />}
      {enableErrorPopup && (
        <ErrorPopup
          message={errMessage}
          title={"Error"}
          exitBtn={closeErrorPopup}
        />
      )}
    </>
  );
}

export default MyCard;
