import Button from "../Button/Button";
import FormInput from "../Form/FormItem";
import SmallExitHeader from "../Header/SmallExitHeader";
import { updateLanguage } from "../../service/LanguageService";
import { useState } from "react";
export default function UpdateLanguage({
  languageId,
  languageName,
  isClose,
  closeUpdateLanguageTab,
  handleEnableSuccessPopup,
  handleEnableErrorPopup,
  setLanguages,
}) {
  const [newLanguageName, setNewLanguageName] = useState(languageName);

  function onSubmit(e) {
    e.preventDefault();
  }

  async function handleUpdateLanguage() {
    const data = await updateLanguage(languageId, newLanguageName);

    if (data.code === 1000) {
      handleEnableSuccessPopup();
      setLanguages((prevLanguages) =>
        prevLanguages.map((language) =>
          language.id === languageId
            ? { ...language, name: newLanguageName }
            : language,
        ),
      );
    } else {
      const message = `code: ${data.code}
                      message: ${data.message}`;

      handleEnableErrorPopup(message);
    }
  }
  return (
    <div
      className={`topic-feature-container ${isClose ? "popup-out" : "popup-in"}`}
    >
      <SmallExitHeader onClick={closeUpdateLanguageTab} />
      <h2>Sửa ngôn ngữ</h2>
      <form action="" onSubmit={onSubmit}>
        <FormInput
          value={newLanguageName}
          onChange={(e) => setNewLanguageName(e.target.value.trim())}
          type="text"
        />
        <Button
          title="Lưu"
          type="submit-blue-btn"
          onClick={handleUpdateLanguage}
        />
      </form>
    </div>
  );
}
