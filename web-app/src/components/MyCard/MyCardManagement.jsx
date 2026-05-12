import ExitHeader from "../Header/ExitHeader";
import CardFeatureBar from "./CardFeatureBar";
import upSlideBtn from "../../assets/chevron-down-solid-full.svg";
import { useEffect, useMemo, useState } from "react";

import { getAllTopics } from "../../service/TopicService";
import { getAllLanguages } from "../../service/LanguageService";
import CardListContainer from "./CardListContainer";

function CardHeaderBar({
  isTopicFilterBtnUp,
  openTopicFilterPopup,
  isLanguageFilterBtnUp,
  openLanguageFilterPopup,
  setSearchTerm,
}) {
  function handleSetSearchTerm(event) {
    const searchValue = event.target.value.trim();

    setSearchTerm(searchValue);
  }

  return (
    <div className="card-management-header">
      <img
        className={
          isLanguageFilterBtnUp ? "rotate-image-open" : "rotate-image-close"
        }
        src={upSlideBtn}
        onClick={openLanguageFilterPopup}
      />
      <img
        className={
          isTopicFilterBtnUp ? "rotate-image-open" : "rotate-image-close"
        }
        src={upSlideBtn}
        onClick={openTopicFilterPopup}
      />
      <input
        type="text"
        className="search-input"
        placeholder="Tìm kiếm"
        onChange={handleSetSearchTerm}
      />
    </div>
  );
}

function FilterPopup({
  containType,
  isDown,
  list,
  setType,
  setEnablePopup,
  setEnableBtn,
}) {
  const [searchTerm, setSearchTerm] = useState("");

  const filteredList = useMemo(() => {
    if (!searchTerm || searchTerm === "") {
      return list;
    }
    return list.filter((item) =>
      item.name.toLowerCase().includes(searchTerm.toLowerCase()),
    );
  }, [list, searchTerm]);

  function handleSelectItem(event) {
    const name = event.target.getAttribute("data-name");
    setType(name);
    setEnablePopup(false);
    setEnableBtn(false);
  }

  function handleSeclectAll() {
    setType("");
    setEnablePopup(false);
    setEnableBtn(false);
  }

  function hanlderSearch(event) {
    const searchValue = event.target.value.trim();
    setSearchTerm(searchValue);
  }

  return (
    <div
      className={`filter-popup ${containType} ${isDown ? "popup-down" : "popup-up"}`}
    >
      <input
        type="text"
        className="search-in-popup"
        onChange={hanlderSearch}
        placeholder="Tìm kiếm"
      />
      <p onClick={handleSeclectAll}>Tất cả</p>
      {filteredList.map((item) => (
        <p
          id={``}
          key={item.id}
          data-name={item.name}
          onClick={handleSelectItem}
        >
          {item.name}
        </p>
      ))}
    </div>
  );
}

function MyCardManagement({ exitBtn, isClose }) {
  // fetch data
  const [languages, setLanguages] = useState([]);
  const [topics, setTopics] = useState([]);

  useEffect(() => {
    async function fetchLanguages() {
      const data = await getAllLanguages();
      if (data.code === 1000) {
        setLanguages(data.body);
      }
    }

    async function fetchTopics() {
      const data = await getAllTopics();
      if (data.code === 1000) {
        setTopics(data.body);
      }
    }

    fetchLanguages();
    fetchTopics();
  }, []);

  // UI variable
  const [isFilterPopupDown, setIsFilterPopupDown] = useState(true);
  const [isTopicFilterBtnUp, setIsTopicFilterBtnUp] = useState(false);
  const [isLanguageFilterBtnUp, setIsLanguageFilterBtnUp] = useState(false);

  const [enableLanguageFilterPopup, setEnableLanguageFilterPopup] =
    useState(false);
  const [enableTopicFilterPopup, setEnableLTopicFilterPopup] = useState(false);

  function openLanguageFilterPopup() {
    if (!isLanguageFilterBtnUp) {
      // close topic filter popup before show
      setIsTopicFilterBtnUp(false);
      setEnableLTopicFilterPopup(false);

      setIsLanguageFilterBtnUp(true);
      setEnableLanguageFilterPopup(true);
    } else {
      setIsFilterPopupDown(false);
      setIsLanguageFilterBtnUp(false);
      setTimeout(() => {
        setEnableLanguageFilterPopup(false);
        setIsFilterPopupDown(true);
      }, 200);
    }
  }

  function openTopicFilterPopup() {
    if (!isTopicFilterBtnUp) {
      // close language popup
      setIsLanguageFilterBtnUp(false);
      setEnableLanguageFilterPopup(false);

      setIsTopicFilterBtnUp(true);
      setEnableLTopicFilterPopup(true);
    } else {
      setIsFilterPopupDown(false);
      setIsTopicFilterBtnUp(false);
      setTimeout(() => {
        setEnableLTopicFilterPopup(false);
        setIsFilterPopupDown(true);
      }, 200);
    }
  }

  // card container list
  const [cards, setCards] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [languageType, setLanguageType] = useState("");
  const [topicType, setTopicType] = useState("");

  const filteredList = useMemo(() => {
    let list = cards;
    if (languageType !== "") {
      list = list.filter((item) =>
        item.language.toLowerCase().includes(languageType.toLowerCase()),
      );
    }
    if (topicType !== "") {
      list = list.filter((item) =>
        item.topic.toLowerCase().includes(topicType.toLowerCase()),
      );
    }

    if (searchTerm !== "") {
      list = list.filter((item) =>
        item.question.toLowerCase().includes(searchTerm.toLowerCase()),
      );
    }

    return list;
  }, [cards, searchTerm, languageType, topicType]);

  return (
    <div
      className={`new-tab-container ${isClose ? "popup-out" : "popup-in"}`}
    >
      <ExitHeader onClick={exitBtn} title="Quản lí thẻ của tôi" />
      <CardHeaderBar
        openLanguageFilterPopup={openLanguageFilterPopup}
        openTopicFilterPopup={openTopicFilterPopup}
        isLanguageFilterBtnUp={isLanguageFilterBtnUp}
        isTopicFilterBtnUp={isTopicFilterBtnUp}
        setSearchTerm={setSearchTerm}
      />
      {enableLanguageFilterPopup && (
        <FilterPopup
          list={languages}
          isDown={isFilterPopupDown}
          containType="language-filter-popup"
          setEnablePopup={setEnableLanguageFilterPopup}
          setType={setLanguageType}
          setEnableBtn={setIsLanguageFilterBtnUp}
        />
      )}
      {enableTopicFilterPopup && (
        <FilterPopup
          list={topics}
          isDown={isFilterPopupDown}
          containType="topic-filter-popup"
          setEnablePopup={setEnableLTopicFilterPopup}
          setType={setTopicType}
          setEnableBtn={setIsTopicFilterBtnUp}
        />
      )}
      <CardListContainer setCards={setCards} filteredList={filteredList} />
    </div>
  );
}

export default MyCardManagement;
