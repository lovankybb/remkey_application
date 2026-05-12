import SmallExitHeader from "../Header/SmallExitHeader";
import FormInput from "../Form/FormItem";
import Button from "../Button/Button";
import { createLanguage } from "../../service/LanguageService";

export default function CreateLanguage({
  isClose,
  closeCreateLanguageTab,
  handleEnableErrorPopup,
  handleEnableSuccessPopup,
  languages,
  setLanguages,
}) {
  function onSubmit(e) {
    e.preventDefault();
  }

  async function handleCreateLanguage() {
    const languageInput = document.querySelector("#language-name");
    const languageName = languageInput.value.trim();
    try {
      const data = await createLanguage(languageName);
      if (data.code === 1000) {
        let newLanguages = languages;
        newLanguages.push(data.body);
        setLanguages(newLanguages);
        languageInput.value = "";
        handleEnableSuccessPopup();
      } else {
        const message = `code: ${data.code}
                        message: ${data.message}`;
        handleEnableErrorPopup(message);
      }
    } catch (err) {
      console.log(err);
    }
  }
  return (
    <div
      className={`topic-feature-container ${isClose ? "popup-out" : "popup-in"}`}
    >
      <SmallExitHeader onClick={closeCreateLanguageTab} />
      <h2>Thêm ngôn ngữ mới</h2>
      <form action="" onSubmit={onSubmit}>
        <FormInput id="language-name" type="text" placeHolder="Ngôn ngữ mới" />
        <Button
          title="Thêm"
          type="submit-blue-btn"
          onClick={handleCreateLanguage}
        />
      </form>
    </div>
  );
}
