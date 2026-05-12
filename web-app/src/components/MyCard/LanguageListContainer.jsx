import { useEffect, useState } from "react";
import editIcon from "../../assets/file-pen-solid-full.svg";
import trashIcon from "../../assets/trash-can-solid-full.svg";
import { getAllLanguages, deleteLanguage } from "../../service/LanguageService";
import ConfirmPopup from "../Popup/ConfirmPopup";
import DisableLayer from "../Popup/DisableLayer";
import SuccessPopup from "../Popup/SuccessPopup";
import ErrorPopup from "../Popup/ErrorPopup";

function LanguageItem({ id,
  name,
  openUpdateLanguageTab,
  updateLanguagesAfterRemoving }) {
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);
  const [enableConfirmPopup, setEnableConfirmPopup] = useState(false);
  const [errMessage, setErrMessage] = useState("");

  function setValueToUpdateLanguage() {
    openUpdateLanguageTab(id, name);
  }

  async function doDeleteLanguage() {
    const data = await deleteLanguage(id);

    if (data.code === 1000) {
      setEnableSuccessPopup(true);
      setTimeout(() => {
        setEnableSuccessPopup(false);
        updateLanguagesAfterRemoving(id);
      }, 2600);

    }
    else {
      const message = `code: ${data.code}
                      message: ${data.message}`;
      setErrMessage(message);
      setEnableErrorPopup(true);
    }
  }




  function showDeleteConfirmPopup() {
    setEnableConfirmPopup(true);
    setEnableDisableLayer(true);
  }

  function closeDeleteConfirmPopup() {
    setEnableConfirmPopup(false);
    setEnableDisableLayer(false);
  }



  return (
    <>
      <div className="list-item">
        <section>
          <h4 id={`topic-${id}`}>{id}</h4>
          <p>{name}</p>
        </section>
        <div>
          <img src={editIcon} alt="Edit" onClick={setValueToUpdateLanguage} />
          <img src={trashIcon} alt=" Delete" onClick={showDeleteConfirmPopup} />
        </div>
      </div>
      {enableConfirmPopup && <ConfirmPopup
        message={"Xác nhận xóa ngôn ngữ"}
        confirm={doDeleteLanguage}
        cancel={closeDeleteConfirmPopup}
      />}
      {enableDisableLayer && <DisableLayer />}
      {enableSuccessPopup && <SuccessPopup />}
      {enableErrorPopup && <ErrorPopup title={"Error"} message={errMessage} />}
    </>
  );
}

export default function LanguageListContainer({
  openUpdateLanguageTab,
  filteredList,
  setLanguages,
}) {


  function updateLanguagesAfterRemoving(languageId) {
    setLanguages(prevLanguages => {
      return prevLanguages.filter(language => language.id !== languageId);
    })
  }

  const [loading, setLoading] = useState(true);

  useEffect(() => {

    try {
      const fetchData = async () => {
        const data = await getAllLanguages();
        if (data.code === 1000) {
          setLanguages(data.body);
        }
      };
      fetchData();
    } catch (err) {
      console.log(err);
    } finally {
      setLoading(false);
    }
  }, []);
  if (loading) return <div className="list-container">Đang tải...</div>;
  return (
    <div className="list-container">
      {filteredList.map((language) => {
        return (
          <LanguageItem
            key={language.id}
            id={language.id}
            name={language.name}
            openUpdateLanguageTab={openUpdateLanguageTab}
            updateLanguagesAfterRemoving={updateLanguagesAfterRemoving}
          />
        );
      })}
    </div>
  );
}
