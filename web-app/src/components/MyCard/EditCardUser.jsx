import { useEffect, useRef, useState } from "react";
import { getAllLanguages } from "../../service/LanguageService";
import { getAllTopics } from "../../service/TopicService";
import Button from "../Button/Button";
import ExitHeader from "../Header/ExitHeader";
import DisableLayerMedium from "../Popup/DisableLayerMedium";
import ErrorPopup from "../Popup/ErrorPopup";
import SelectionPopup from "./SelectionPopup";
import { uploadImageToCloud } from "../../service/CloudinaryService";
import {
  updateCardUser,
  updateCardUserImage,
} from "../../service/CardUserService";
import SuccessPopup from "../Popup/SuccessPopup";
import { getTopicByName } from "../../service/TopicService";
import { getLanguageByName } from "../../service/LanguageService";

function EditCard({
  exitBtn,
  isClose,
  id,
  oldQuestion,
  oldAnswer,
  languageType,
  topicType,
  setCards,
}) {
  // Card creation variable
  const [topicId, setTopicId] = useState("");
  const [languageId, setLanguageId] = useState("");
  const [question, setQuestion] = useState(oldQuestion);
  const [answer, setAnswer] = useState(oldAnswer);

  // UI variable
  const [isCloseTab, setIsCloseTab] = useState(false);
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);

  const [topics, setTopics] = useState([]);
  const [topicName, setTopicName] = useState("Chọn chủ đề");
  const [enableSelectTopicTab, setEnableSelectTopicTab] = useState(false);

  const [languages, setLanguages] = useState([]);
  const [enableSelectLanguageTab, setEnableSelectLanguageTab] = useState(false);
  const [languageName, setLanguageName] = useState("Chọn ngôn ngữ");

  const [imageName, setImageName] = useState("Cập nhật ảnh mới");
  const imageInpuRef = useRef(null);
  const [uploadImage, setUploadImage] = useState(null);

  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [errMessage, setErrMessage] = useState("");

  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);

  function preventDefaultSubmit(e) {
    e.preventDefault();
  }

  function closeErrorPopup() {
    setEnableErrorPopup(false);
    setEnableDisableLayer(false);
  }

  function handleShowErrorPopup(message) {
    setErrMessage(message);
    setEnableErrorPopup(true);
  }

  function handleShowSuccessPopup() {
    setEnableSuccessPopup(true);
    setTimeout(() => {
      setEnableSuccessPopup(false);
      setEnableDisableLayer(false);
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

  // illustrative image

  const openSelectImage = () => {
    imageInpuRef.current.click();
  };

  const handleSetImage = (event) => {
    const image = event.target.files[0];
    console.log("files", image);
    setImageName(image.name);
    setUploadImage(image);
  };

  // edit

  async function handleEditCard() {
    setEnableDisableLayer(true);

    const data = await updateCardUser(
      id,
      question,
      answer,
      languageId,
      topicId,
    );

    if (data.code === 1000) {
      const newCard = data.body;

      setCards((prevCards) =>
        prevCards.map((card) => {
          if (card.id === id) {
            return { ...newCard };
          }
          return card;
        }),
      );

      if (uploadImage) {
        const cloudRes = await uploadImageToCloud(uploadImage);

        const imageId = newCard.mainImage.id;
        const newUrl = cloudRes.secure_url;
        const publicId = cloudRes.public_id;

        await updateCardUserImage(imageId, newUrl, publicId);
      }

      handleShowSuccessPopup();
    } else {
      const message = `code: ${data.code}
                      message: ${data.message}`;
      handleShowErrorPopup(message);
    }
  }

  // init value for card before custome

  useEffect(() => {
    const loadData = async () => {
      const fetchLanguage = async () => {
        const data = await getLanguageByName(languageType);
        if (data.code === 1000) {
          setValueForLanguage(data.body.id, data.body.name);
        }
      };
      const fetchTopic = async () => {
        const data = await getTopicByName(topicType);
        if (data.code === 1000) {
          setValueForTopic(data.body.id, data.body.name);
        }
      };

      await fetchLanguage();
      await fetchTopic();
    };

    loadData();
  }, []);

  return (
    <>
      <div
        className={`new-tab-container ${isClose ? "popup-out" : "popup-in"}`}
      >
        <ExitHeader onClick={exitBtn} title="Chỉnh sửa thẻ" />
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
            <p className="open-selection-btn" onClick={openSelectImage}>
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
            value={question}
            placeholder={question}
            onChange={(e) => setQuestion(e.target.value)}
          />
          <textarea
            className="card-part"
            value={answer}
            placeholder={answer}
            onChange={(e) => setAnswer(e.target.value)}
          />
          <Button
            onClick={handleEditCard}
            type="blue-btn"
            title="Lưu thay đổi"
          />
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

export default EditCard;
