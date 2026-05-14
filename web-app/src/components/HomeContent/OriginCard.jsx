import { useState } from "react";
import addBtn from "../../assets/download-solid-full.svg";
import moreBtn from "../../assets/ellipsis-vertical-solid-full.svg";
import defaultImg from "../../assets/image-regular-full.svg";
import speakerIcon from "../../assets/volume-solid-full.svg";
import { addCardToMyList } from "../../service/CardUserService";
import ConfirmPopup from "../Popup/ConfirmPopup";
import SuccessPopup from "../Popup/SuccessPopup";
import ErrorPopup from "../Popup/ErrorPopup";
import DisableLayer from "../Popup/DisableLayer";
import OriginCardMenu from "./OriginCardMenu";
import ReportContainer from "./ReportContainer";
import { speak } from "../../service/SoundService";
const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export default function OriginCard({
  id,
  question,
  answer,
  language,
  topic,
  mainImage,
}) {
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);
  const [enableConfirmPopup, setEnableConfirmPopup] = useState(false);
  const [enableOriginMenuPopup, setEnableOriginMenuPopup] = useState(false);
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const [enableReportPopup, setEnableReportPopup] = useState(false);

  function openAddToListConfirmPopup() {
    setEnableDisableLayer(true);
    setEnableConfirmPopup(true);
  }

  function closeAddToListConfirmPopup() {
    setEnableDisableLayer(false);
    setEnableConfirmPopup(false);
  }

  function openOriginCardMenu() {
    if (enableOriginMenuPopup) {
      setEnableOriginMenuPopup(false);
    } else {
      setEnableOriginMenuPopup(true);
    }
  }

  const openErrorPopup = (message) => {
    setErrorMessage(message);
    setEnableErrorPopup(true);
  };

  const closeErrorPopup = () => {
    setEnableDisableLayer(false);
    setEnableErrorPopup(false);
  };

  const openSuccessPopup = () => {
    setEnableSuccessPopup(true);
    setTimeout(() => {
      setEnableSuccessPopup(false);
      setEnableDisableLayer(false);
    }, 2600);
  };

  const doAddingToMyList = async () => {
    setEnableConfirmPopup(false);

    const data = await addCardToMyList(id);
    if (data.code === 1000) {
      openSuccessPopup();
    } else {
      const message = `code: ${data.code}
                      message: ${data.message}`;
      openErrorPopup(message);
    }
  };

  const handleSpeak = () => {
    speak(question, language);
  };

  const handleOpenReportPopup = () => {
    setEnableDisableLayer(true);
    setEnableReportPopup(true);
  };

  const handleCloseReportPopup = () => {
    setEnableDisableLayer(false); 
    setEnableReportPopup(false);
  };

  return (
    <>
      <div className="origin-card">
        <div className="origin-card-header">
          <img src={addBtn} alt="" onClick={openAddToListConfirmPopup} />
          <img src={moreBtn} alt="" onClick={openOriginCardMenu} />
          {enableOriginMenuPopup && (
            <OriginCardMenu
              openReportPopup={handleOpenReportPopup}
              language={language}
              topic={topic}
            />
          )}
        </div>
        <hr />
        <img
          className="illustrate-img "
          src={
              mainImage
              ? mainImage.url
              : defaultImg
          }
          alt="Ảnh minh họa"
        />
        <div className="sound-container">
          <img
            onClick={handleSpeak}
            src={speakerIcon}
            alt=""
            className="speaker-icon"
          />
        </div>
        <section>
          <h2>{question}</h2>
          <br />
          <p>{answer}</p>
        </section>
      </div>
      {enableDisableLayer && <DisableLayer />}
      {enableConfirmPopup && (
        <ConfirmPopup
          message="Xác nhận thêm vào danh sách học."
          cancel={closeAddToListConfirmPopup}
          confirm={doAddingToMyList}
        />
      )}
      {enableSuccessPopup && <SuccessPopup />}
      {enableErrorPopup && (
        <ErrorPopup
          title={"Error"}
          message={errorMessage}
          onClick={closeErrorPopup}
        />
      )}
      {enableReportPopup && <ReportContainer closeReportPopup={handleCloseReportPopup} cardId={id} />}
    </>
  );
}
