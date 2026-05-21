import SmallExitHeader from "../Header/SmallExitHeader";
import FormInput from "../Form/FormItem";
import Button from "../Button/Button";
import { createLanguage } from "../../service/LanguageService";
import { useState } from "react";

export default function CreateLanguage({
  isClose,
  closeCreateLanguageTab,
  handleEnableErrorPopup,
  handleEnableSuccessPopup,
  languages,
  setLanguages,
}) {
   const [languageName, setLanguageName] = useState(''); 

  function onSubmit(e) {
    e.preventDefault();
  }

  async function handleCreateLanguage() {
    
    try {
      const data = await createLanguage(languageName);
      if (data.code === 1000) {
        let newLanguages = languages;
        newLanguages.push(data.body);
        setLanguages(newLanguages);
        setLanguageName(""); 
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
        <FormInput value={languageName} onChange={(e)=> setLanguageName(e.target.value.trim())} type="text" placeHolder="Ngôn ngữ mới" />
        <Button
          title="Thêm"
          type="submit-blue-btn"
          onClick={handleCreateLanguage}
        />
      </form>
    </div>
  );
}
