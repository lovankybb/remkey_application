import { useState } from "react";
import { updateTopic } from "../../service/TopicService";
import Button from "../Button/Button";
import FormInput from "../Form/FormItem";
import SmallExitHeader from "../Header/SmallExitHeader";

function UpdateTopic({
  closeUpdateTopicTab,
  isClose,
  topicId,
  topicName,
  enableSuccessPopup,
  enablErrorPopup,
  setTopics,
}) {


  const [newTopicName, setNewTopicName] = useState(topicName); 

  function onSubmit(e) {
    e.preventDefault();
  }



  async function handleUpdateTopic() {

    try {
      const data = await updateTopic(topicId, newTopicName);

      if (data.code === 1000) {
        enableSuccessPopup();
        setTopics((prevTopics) =>
          prevTopics.map((topic) =>
            topic.id === topicId ? { ...topic, name: newTopicName } : topic,
          ),
        );
      } else {
        const message = `code: ${data.code}
                        message: ${data.message}`;

        enablErrorPopup(message);
      }
    } catch (err) {
      console.log(err);
    }
  }

  return (
    <div
      className={`topic-feature-container ${isClose ? "popup-out" : "popup-in"}`}
    >
      <SmallExitHeader onClick={closeUpdateTopicTab} />
      <h2>Chỉnh sửa chủ đề</h2>
      <form action="" onSubmit={onSubmit}>
        <FormInput value={newTopicName} onChange={(e)=> setNewTopicName(e.target.value.trim())} type="text" placeHolder={topicName} />
        <Button
          title="Lưu"
          type="submit-blue-btn"
          onClick={handleUpdateTopic}
        />
      </form>
    </div>
  );
}

export default UpdateTopic;
