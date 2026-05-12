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
  function onSubmit(e) {
    e.preventDefault();
  }

  async function handleCreateTopic() {
    const input = document.querySelector("#topic-name");
    try {
      const data = await createTopic(input.value.trim());

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
      input.value = "";
    }
  }

  return (
    <div
      className={`topic-feature-container ${isClose ? "popup-out" : "popup-in"}`}
    >
      <SmallExitHeader onClick={closeCreateTopicTab} />
      <h2>Thêm chủ đề</h2>
      <form action="" onSubmit={onSubmit}>
        <FormInput id="topic-name" type="text" placeHolder="Chủ đề mới" />
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
