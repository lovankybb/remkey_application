import { useMemo, useState } from "react";
import ExitHeader from "../Header/ExitHeader";
import DisableLayer from "../Popup/DisableLayer";
import ErrorPopup from "../Popup/ErrorPopup";
import SuccessPopup from "../Popup/SuccessPopup";
import CardFeatureBar from "./CardFeatureBar";
import CreateLanguage from "./CreateLanguge";
import LanguageListContainer from "./LanguageListContainer";
import UpdateLanguage from "./UpdateLanguage";

function Language({ exitBtn, isClose }) {
  const [languages, setLanguages] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);
  const [enableUpdateLanguage, setEnablUpdateLanguage] = useState(false);
  const [errMessage, setErrMessage] = useState("");
  const [enableCreateLanguageTab, setEnableCreateLanguageTab] = useState(false);
  const [isCloseTab, setIsCloseTab] = useState(false);
  const [updateLanguageId, setUpdateLanguageId] = useState("");
  const [updateLanguageName, setUpdateLanguageName] = useState("");

  // filtered varible
  const filteredList = useMemo(() => {
    if (searchTerm === "" || searchTerm === null) {
      return languages;
    }
    return languages.filter((language) =>
      language.name.toLowerCase().includes(searchTerm.toLowerCase()),
    );
  }, [searchTerm, languages]);

  function handleEnableSuccessPopup() {
    setEnableSuccessPopup(true);
    setTimeout(() => {
      setEnableSuccessPopup(false);
    }, 2600);
  }

  function handleEnableErrorPopup(message) {
    setErrMessage(message);
    setEnableErrorPopup(true);
  }

  function closeErrorPopup() {
    setEnableErrorPopup(false);
  }

  function openCreateLanguageTab() {
    setEnableDisableLayer(true);
    setEnableCreateLanguageTab(true);
  }

  function closeCreateLanguageTab() {
    setIsCloseTab(true);
    setTimeout(() => {
      setEnableCreateLanguageTab(false);
      setEnableDisableLayer(false);
      setIsCloseTab(false);
    }, 150);
  }

  function openUpdateLanguageTab(languageId, languageName) {
    setUpdateLanguageId(languageId);
    setUpdateLanguageName(languageName);
    setEnableDisableLayer(true);
    setEnablUpdateLanguage(true);
  }

  function closeUpdateLanguageTab() {
    setIsCloseTab(true);
    setTimeout(() => {
      setEnablUpdateLanguage(false);
      setEnableDisableLayer(false);
      setIsCloseTab(false);
    }, 200);
  }

  return (
    <>
      <div
        className={`new-tab-container ${isClose ? "popup-out" : "popup-in"}`}
      >
        <ExitHeader onClick={exitBtn} title="Quản lí ngôn ngữ" />
        <CardFeatureBar
          openPlusTab={openCreateLanguageTab}
          setSearchTerm={setSearchTerm}
        />

        <LanguageListContainer
          filteredList={filteredList}
          setLanguages={setLanguages}
          openUpdateLanguageTab={openUpdateLanguageTab}
        />
        {enableDisableLayer && <DisableLayer />}
        {enableSuccessPopup && <SuccessPopup />}
        {enableErrorPopup && <ErrorPopup title={"Error"} message={errMessage} onClick={closeErrorPopup} />}
        {enableCreateLanguageTab && (
          <CreateLanguage
            isClose={isCloseTab}
            closeCreateLanguageTab={closeCreateLanguageTab}
            languages={languages}
            setLanguages={setLanguages}
            handleEnableSuccessPopup={handleEnableSuccessPopup}
            handleEnableErrorPopup={handleEnableErrorPopup}
          />
        )}
        {enableUpdateLanguage && (
          <UpdateLanguage
            languageId={updateLanguageId}
            languageName={updateLanguageName}
            closeUpdateLanguageTab={closeUpdateLanguageTab}
            isClose={isCloseTab}
            handleEnableSuccessPopup={handleEnableSuccessPopup}
            handleEnableErrorPopup={handleEnableErrorPopup}
            setLanguages={setLanguages}
          />
        )}
      </div>
    </>
  );
}

export default Language;
