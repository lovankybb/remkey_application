import { useState } from "react";
import { createTopic } from "../../service/TopicService";
import Button from "../Button/Button";
import FormInput from "../Form/FormItem";
import SmallExitHeader from "../Header/SmallExitHeader";

function CreateTopic({
  closeCreateTopicTab,
  isClose,
  enableSuccessPopup,
  enablErrorPopup,
  topics,
  setTopics,
}) {

  const [topicName, setTopicName] = useState("");

  function onSubmit(e) {
    e.preventDefault();
  }

  async function handleCreateTopic() {
  
    try {
      const data = await createTopic(topicName);

      if (data.code === 1000) {
        const topic = data.body;
        let newTopic = topics;
        newTopic.push(topic);
        setTopics(newTopic);

        enableSuccessPopup();
      } else {
        const message = `code: ${data.code}
                      message: ${data.message}`;
        enablErrorPopup(message);
      }
    } catch (error) {
      console.log(error);
    } finally {
      setTopicName(""); 
    }
  }

  return (
    <div
      className={`topic-feature-container ${isClose ? "popup-out" : "popup-in"}`}
    >
      <SmallExitHeader onClick={closeCreateTopicTab} />
      <h2>Thêm chủ đề</h2>
      <form action="" onSubmit={onSubmit}>
        <FormInput value={topicName} onChange={(e)=> setTopicName(e.target.value.trim())} type="text" placeHolder="Chủ đề mới" />
        <Button
          title="Thêm"
          type="submit-blue-btn"
          onClick={handleCreateTopic}
        />
      </form>
    </div>
  );
}

export default CreateTopic;
