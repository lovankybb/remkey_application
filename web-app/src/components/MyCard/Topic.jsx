import { useMemo, useState } from "react";
import ExitHeader from "../Header/ExitHeader";
import DisableLayer from "../Popup/DisableLayer";
import ErrorPopup from "../Popup/ErrorPopup";
import SuccessPopup from "../Popup/SuccessPopup";
import CardFeatureBar from "./CardFeatureBar";
import CreateTopic from "./CreateTopic";
import ListContainer from "./TopicListContainer";
import UpdateTopic from "./UpdateTopic";
import TopicListContainer from "./TopicListContainer";

function Topic({ exitBtn, isClose }) {
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);
  const [enableCreateTopicTab, setEnableCreateTopicTab] = useState(false);
  const [enableUpdateTopicTab, setEnableUpdateTopicTab] = useState(false);
  const [currUpdateTopicId, setCurrUpdateTopicId] = useState("");
  const [currUpdateTopicName, setCurrUpdateTopicName] = useState("");
  const [isCloseTab, setIsCloseTab] = useState(false);
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);
  const [errMessage, setErrMessage] = useState("");

  const [topics, setTopics] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredList = useMemo(() => {
    if (searchTerm === "" || searchTerm === null) {
      return topics;
    }
    return topics.filter((topic) =>
      topic.name.toLowerCase().includes(searchTerm.toLowerCase()),
    );
  }, [searchTerm, topics]);

  function openCreateTopicTab() {
    setEnableDisableLayer(true);
    setEnableCreateTopicTab(true);
  }

  function closeCreateTopicTab() {
    setIsCloseTab(true);
    setTimeout(() => {
      setEnableDisableLayer(false);
      setEnableCreateTopicTab(false);
      setIsCloseTab(false);
    }, 150);
  }
  function closeUpdateTopicTab() {
    setIsCloseTab(true);
    setTimeout(() => {
      setEnableDisableLayer(false);
      setEnableUpdateTopicTab(false);
      setIsCloseTab(false);
    }, 150);
  }

  function handleEnablErrorPopup(message) {
    setErrMessage(message);
    setEnableErrorPopup(true);
  }

  function handleEnableSuccessPopup() {
    setEnableSuccessPopup(true);
    setTimeout(() => {
      setEnableSuccessPopup(false);
    }, 2600);
  }

  function closeErrorPopup() {
    setEnableErrorPopup(false);
  }

  function openUpdateTopicTab(topicId, topicName) {
    setCurrUpdateTopicId(topicId);
    setCurrUpdateTopicName(topicName);
    setEnableDisableLayer(true);
    setEnableUpdateTopicTab(true);
  }

  return (
    <div className={`new-tab-container ${isClose ? "popup-out" : "popup-in"}`}>
      <ExitHeader onClick={exitBtn} title="Quản lí chủ đề" />

      <CardFeatureBar
        openPlusTab={openCreateTopicTab}
        setSearchTerm={setSearchTerm}
      />

      {/* Render */}
      <h2>Danh sách chủ đề</h2>
      <TopicListContainer
        enablErrPopup={handleEnablErrorPopup}
        topics={topics}
        setTopics={setTopics}
        filteredList={filteredList}
        openUpdateTopicTab={openUpdateTopicTab}
      />
      {enableDisableLayer && <DisableLayer />}

      {/* Create */}
      {enableCreateTopicTab && (
        <CreateTopic
          isClose={isCloseTab}
          closeCreateTopicTab={closeCreateTopicTab}
          enablErrorPopup={handleEnablErrorPopup}
          enableSuccessPopup={handleEnableSuccessPopup}
          topics={topics}
          setTopics={setTopics}
        />
      )}

      {/* Update */}
      {enableUpdateTopicTab && (
        <UpdateTopic
          topicId={currUpdateTopicId}
          topicName={currUpdateTopicName}
          enablErrorPopup={handleEnablErrorPopup}
          enableSuccessPopup={handleEnableSuccessPopup}
          setTopics={setTopics}
          closeUpdateTopicTab={closeUpdateTopicTab}
        />
      )}

      {enableErrorPopup && (
        <ErrorPopup
          title="Error"
          message={errMessage}
          onClick={closeErrorPopup}
        />
      )}
      {enableSuccessPopup && <SuccessPopup />}
    </div>
  );
}

export default Topic;
