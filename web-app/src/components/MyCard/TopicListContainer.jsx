import trashIcon from "../../assets/trash-can-solid-full.svg";
import editIcon from "../../assets/file-pen-solid-full.svg";
import { useEffect, useState } from "react";
import { getAllTopics } from "../../service/TopicService";
import ConfirmPopup from "../Popup/ConfirmPopup";
import DisableLayer from "../Popup/DisableLayer";
import { deleteTopicById } from "../../service/TopicService";
import SuccessPopup from "../Popup/SuccessPopup";
import ErrorPopup from "../Popup/ErrorPopup";

function TopicItem({ id, name, updateListAfterRemoving, openUpdateTopicTab }) {
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);
  const [enableConfirmPopup, setEnableConfirmPopup] = useState(false);
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [errMessage, setErrMessage] = useState("");

  function showConfirmPopup() {
    setEnableConfirmPopup(true);
    setEnableDisableLayer(true);
  }

  function exitConfirmPopup() {
    setEnableConfirmPopup(false);
    setEnableDisableLayer(false);
  }

  async function doDeletingConfirm() {
    setEnableConfirmPopup(false);

    try {
      const data = await deleteTopicById(id);
      if (data.code === 1000) {
        updateListAfterRemoving(id);
        setEnableSuccessPopup(true);
        setTimeout(() => {
          setEnableSuccessPopup(false);
          setEnableDisableLayer(false);
        }, 2600);
      } else {
        const message = `code: ${data.code}
                        message: ${data.message}`;
        setErrMessage(message);
      }
    } catch (err) {
      console.log(err);
    }
  }

  function exitErrorPopup() {
    setEnableErrorPopup(false);
  }

  function handleDeleteTopic() {
    showConfirmPopup();
  }

  function setValueToUpdateTopic() {
    openUpdateTopicTab(id, name);
  }

  return (
    <>
      <div className="list-item">
        <section>
          <h4 id={`topic-${id}`}>{id}</h4>
          <p>{name}</p>
        </section>
        <div>
          <img src={editIcon} alt="Edit" onClick={setValueToUpdateTopic} />
          <img src={trashIcon} alt="Delete" onClick={handleDeleteTopic} />
        </div>
      </div>
      {enableDisableLayer && <DisableLayer />}
      {enableConfirmPopup && (
        <ConfirmPopup
          message="Xác nhận xóa chủ đề!"
          confirm={doDeletingConfirm}
          cancel={exitConfirmPopup}
        />
      )}
      {enableSuccessPopup && <SuccessPopup />}
      {enableErrorPopup && (
        <ErrorPopup
          title="Error"
          message={errMessage}
          onClick={exitErrorPopup}
        />
      )}
    </>
  );
}

export default function TopicListContainer({
  enableErrorPopup,
  topics,
  setTopics,
  filteredList,
  openUpdateTopicTab,
}) {
  const [loading, setLoading] = useState(true);

  function removeTopic(topicId) {
    let newTopic = topics.filter((topic) => topic.id !== topicId);
    setTopics(newTopic);
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getAllTopics();

        if (data.code === 1000) {
          setTopics(data.body);
        } else {
          const message = `code: ${data.code}
                          message: ${data.message}`;
          enableErrorPopup(message);
        }
      } catch (err) {
        console.log(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);
  if (loading) return <div className="list-container">Đang tải...</div>;
  return (
    <>
      <div className="list-container">
        {filteredList.map((topic) => {
          return (
            <TopicItem
              key={topic.id}
              id={topic.id}
              name={topic.name}
              updateListAfterRemoving={removeTopic}
              openUpdateTopicTab={openUpdateTopicTab}
            />
          );
        })}
      </div>
    </>
  );
}
