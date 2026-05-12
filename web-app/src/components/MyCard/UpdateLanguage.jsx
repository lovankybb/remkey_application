import Button from "../Button/Button";
import FormInput from "../Form/FormItem";
import SmallExitHeader from "../Header/SmallExitHeader";
import { updateLanguage } from "../../service/LanguageService";
export default function UpdateLanguage({
  languageId,
  languageName,
  isClose,
  closeUpdateLanguageTab,
  handleEnableSuccessPopup,
  handleEnableErrorPopup,
  setLanguages
}) {
  function onSubmit(e) {
    e.preventDefault();
  }

  async function handleUpdateLanguage() {

    const nameInput = document.querySelector('#language-name');

    const newLanguageName = nameInput.value.trim();
    const data = await updateLanguage(languageId, newLanguageName);

    if (data.code === 1000) {
      handleEnableSuccessPopup();
      setLanguages(prevLanguages =>
        prevLanguages.map((language) =>
          language.id === languageId
            ? { ...language, name: newLanguageName } : language
        )
      );
    }
    else {
      const message = `code: ${data.code}
                      message: ${data.message}`

      handleEnableErrorPopup(message)
    }

  }
  return (
    <div
      className={`topic-feature-container ${isClose ? "popup-out" : "popup-in"}`}
    >
      <SmallExitHeader onClick={closeUpdateLanguageTab} />
      <h2>Sửa ngôn ngữ</h2>
      <form action="" onSubmit={onSubmit}>
        <FormInput id="language-name" type="text" placeHolder={languageName} />
        <Button
          title="Thêm"
          type="submit-blue-btn"
          onClick={handleUpdateLanguage}
        />
      </form>
    </div>
  );
}
