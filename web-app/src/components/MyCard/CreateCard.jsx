import { useRef, useState } from "react";
import { getAllLanguages } from "../../service/LanguageService";
import { getAllTopics } from "../../service/TopicService";
import Button from "../Button/Button";
import ExitHeader from "../Header/ExitHeader";
import DisableLayerMedium from "../Popup/DisableLayerMedium";
import ErrorPopup from "../Popup/ErrorPopup";
import SelectionPopup from "./SelectionPopup";
import UploadImagePopup from "../Popup/UploadImagePopup";
import { createCard, uploadCardImage } from "../../service/CardSevice";
import SuccessPopup from "../Popup/SuccessPopup";

function CreateCard({ exitBtn, isClose }) {
  // Card creation variable
  const [topicId, setTopicId] = useState("");
  const [languageId, setLanguageId] = useState("");

  // UI variable
  const [isCloseTab, setIsCloseTab] = useState(false);
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);

  const [topics, setTopics] = useState([]);
  const [topicName, setTopicName] = useState("Chọn chủ đề");
  const [enableSelectTopicTab, setEnableSelectTopicTab] = useState(false);

  const [languages, setLanguages] = useState([]);
  const [enableSelectLanguageTab, setEnableSelectLanguageTab] = useState(false);
  const [languageName, setLanguageName] = useState("Chọn ngôn ngữ");

  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [errMessage, setErrMessage] = useState("");

  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);

  const imageInpuRef = useRef(null);
  const [imageName, setImageName] = useState("Thêm ảnh minh họa");
  const [uploadImage, setUploadImage] = useState(null);

  function preventDefaultSubmit(e) {
    e.preventDefault();
  }

  function closeErrorPopup() {
    setEnableErrorPopup(false);
  }

  function handleShowErrorPopup(message) {
    setErrMessage(message);
    setEnableErrorPopup(true);
  }

  function handleShowSuccessPopup() {
    setEnableSuccessPopup(true);
    setTimeout(() => {
      setEnableSuccessPopup(false);
    }, 2600);
  }

  // select topic
  async function openSelectTopicTab() {
    setEnableDisableLayer(true);
    const data = await getAllTopics();

    if (data.code !== 1000) {
      const message = `code: ${data.code}
                        message: ${data.message}`;
      handleShowErrorPopup(message);
    }
    setTopics(data.body);
    setEnableSelectTopicTab(true);
  }

  function closeSelectTopicTab() {
    setIsCloseTab(true);
    setTimeout(() => {
      setEnableDisableLayer(false);
      setEnableSelectTopicTab(false);
      setIsCloseTab(false);
    }, 200);
  }

  function setValueForTopic(topicId, topicName) {
    setTopicId(topicId);
    setTopicName(topicName);
    closeSelectTopicTab();
  }

  // select language
  function closeSelectLanguageTab() {
    setIsCloseTab(true);
    setTimeout(() => {
      setEnableDisableLayer(false);
      setEnableSelectLanguageTab(false);
      setIsCloseTab(false);
    }, 200);
  }

  async function openSelectLanguageTab() {
    setEnableDisableLayer(true);
    const data = await getAllLanguages();
    if (data.code !== 1000) {
      const message = `code: ${data.code}
                        message: ${data.message}`;
      handleShowErrorPopup(message);
    }
    setLanguages(data.body);
    setEnableSelectLanguageTab(true);
  }

  function setValueForLanguage(languageId, languageName) {
    setLanguageId(languageId);
    setLanguageName(languageName);
    closeSelectLanguageTab();
  }

  const openImageInputTab = () => {
    imageInpuRef.current.click();
  };

  const handleSetImage = (event) => {
    const image = event.target.files[0];
    console.log("files", image);
    setImageName(image.name);
    setUploadImage(image);
  };

  // create

  async function handleCreateCard() {
    const questionInput = document.querySelector("#question");
    const answerInput = document.querySelector("#answer");

    const question = questionInput.value.trim();
    const answer = answerInput.value.trim();

    if (!question || question === "") {
      const message = "Mặt trước của thẻ đang trống";
      handleShowErrorPopup(message);
      return;
    }

    if (!answer || answer === "") {
      const message = "Mặt sau của thẻ đang trống";
      handleShowErrorPopup(message);
      return;
    }

    if (!topicId || topicId === "") {
      const message = "Bạn chưa chọn chủ đề";
      handleShowErrorPopup(message);
      return;
    }

    if (!languageId || languageId === "") {
      const message = "Bạn chưa chọn ngôn ngữ";
      handleShowErrorPopup(message);
      return;
    }

    const data = await createCard(question, answer, languageId, topicId);

    if (data.code === 1000) {
      if (uploadImage !== null) {
        uploadCardImage(data.body.id, uploadImage);
      }

      handleShowSuccessPopup();
    } else {
      const message = `code: ${data.code}
                      message: ${data.message}`;
      handleShowErrorPopup(message);
    }
  }

  return (
    <>
      <div
        className={`new-tab-container ${isClose ? "popup-out" : "popup-in"}`}
      >
        <ExitHeader onClick={exitBtn} title="Tạo thẻ mới" />
        <form onSubmit={preventDefaultSubmit}>
          <div>
            <p onClick={openSelectLanguageTab} className="open-selection-btn">
              {languageName}
            </p>
          </div>
          <div>
            <p onClick={openSelectTopicTab} className="open-selection-btn">
              {topicName}
            </p>
          </div>
          <div>
            <p className="open-selection-btn" onClick={openImageInputTab}>
              {imageName}
            </p>
            <input
              onChange={handleSetImage}
              ref={imageInpuRef}
              type="file"
              style={{ display: "none" }}
            />
          </div>
          <textarea
            className="card-part"
            id="question"
            placeholder="Mặt trước"
          />
          <textarea className="card-part" id="answer" placeholder="Mặt sau" />
          <Button onClick={handleCreateCard} type="blue-btn" title="Tạo thẻ" />
        </form>
      </div>
      {enableSelectTopicTab && (
        <SelectionPopup
          type="topic"
          objects={topics}
          isClose={isCloseTab}
          setValueFor={setValueForTopic}
          closeSelectionTab={closeSelectTopicTab}
        />
      )}
      {enableSelectLanguageTab && (
        <SelectionPopup
          type="language"
          objects={languages}
          isClose={isCloseTab}
          setValueFor={setValueForLanguage}
          closeSelectionTab={closeSelectLanguageTab}
        />
      )}
      {enableDisableLayer && <DisableLayerMedium />}
      {enableErrorPopup && (
        <ErrorPopup
          title={"Error"}
          message={errMessage}
          onClick={closeErrorPopup}
        />
      )}

      {enableSuccessPopup && <SuccessPopup />}
    </>
  );
}

export default CreateCard;
