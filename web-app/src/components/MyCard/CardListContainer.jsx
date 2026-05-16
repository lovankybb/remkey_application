import deleteIcon from "../../assets/trash-can-solid-full.svg";
import editIcon from "../../assets/file-pen-solid-full.svg";
import { useEffect, useState } from "react";
import { getAllCardUsers, deleteCardUser } from "../../service/CardUserService";
import CardUserDetailPopup from "./CardUserDetailPopup";
import DisableLayer from "../Popup/DisableLayer";
import ConfirmPopup from "../Popup/ConfirmPopup";
import SuccessPopup from "../Popup/SuccessPopup";
import ErrorPopup from "../Popup/ErrorPopup";
import EditCardUser from "./EditCardUser";

function CardItem({
  id,
  question,
  answer,
  language,
  topic,
  mainImage,
  handleFilteredListAfterRemoving,
  setCards
}) {
  const [enableCardDetail, setEnableCardDetail] = useState(false);
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);
  const [isCloseTab, setIsCloseTab] = useState(false);
  const [enableDeleteConfirm, setEnableDeleteConfirm] = useState(false);
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [errMessage, setErrMessage] = useState("");
  const [enableEditCardTab, setEnableEditCardTab] = useState(false);

  function openDetailCard() {
    setEnableDisableLayer(true);
    setEnableCardDetail(true);
  }

  function closeDetailCard() {
    setIsCloseTab(true);
    setTimeout(() => {
      setEnableCardDetail(false);
      setEnableDisableLayer(false);
      setIsCloseTab(false);
    }, 200);
  }

  function showDeleteConfirm() {
    setEnableDisableLayer(true);
    setEnableDeleteConfirm(true);
  }

  function cancelDeleteCard() {
    setTimeout(() => {
      setEnableDisableLayer(false);
      setEnableDeleteConfirm(false);
    }, 200);
  }

  async function doDeleteCardUser() {
    try {
      const data = await deleteCardUser(id);
      if (data.code === 1000) {
        handleFilteredListAfterRemoving(id);
        setEnableDeleteConfirm(false);
        setEnableSuccessPopup(true);
        setTimeout(() => {
          setEnableSuccessPopup(false);
          setEnableDisableLayer(false);
        }, 2600);
      } else {
        const message = `code: ${data.code}
                        message: ${data.message}`;
        setErrMessage(message);
        setEnableErrorPopup(true);
      }
    } catch (err) {
      console.log(err);
    }
  }

  function closeErrorPopup() {
    setEnableErrorPopup(false);
  }

  function openEditTab() {
    setEnableEditCardTab(true);
  }

  function closeEditTab() {
    setIsCloseTab(true);
    setTimeout(() => {
      setEnableEditCardTab(false);
      setIsCloseTab(false);
    }, 200);
  }

  return (
    <>
      <div className="card-item">
        <p onClick={openDetailCard}>{question}</p>
        <div>
          <img src={editIcon} alt="Chỉnh sửa" onClick={openEditTab} />
          <img src={deleteIcon} alt="Xoá" onClick={showDeleteConfirm} />
        </div>
      </div>
      {enableDisableLayer && <DisableLayer />}
      {enableCardDetail && (
        <CardUserDetailPopup
          isClose={isCloseTab}
          question={question}
          answer={answer}
          mainImage={mainImage}
          language={language}
          topic={topic}
          close={closeDetailCard}
        />
      )}
      {enableDeleteConfirm && (
        <ConfirmPopup
          isClose={isCloseTab}
          message="Xác nhận muốn xóa thẻ"
          cancel={cancelDeleteCard}
          confirm={doDeleteCardUser}
        />
      )}
      {enableSuccessPopup && <SuccessPopup />}
      {enableErrorPopup && (
        <ErrorPopup
          message={errMessage}
          title={"Error"}
          onClick={closeErrorPopup}
        />
      )}
      {enableEditCardTab && (
        <EditCardUser
          id={id}
          oldQuestion={question}
          oldAnswer={answer}
          languageType={language}
          topicType={topic}
          isClose={isCloseTab}
          exitBtn={closeEditTab}
          setCards={setCards}
        />
      )}
    </>
  );
}

export default function CardListContainer({ filteredList, setCards }) {
  const [loading, setLoading] = useState(true);

  function handleFilteredListAfterRemoving(cardId) {
    setCards((prevCards) => prevCards.filter((card) => card.id !== cardId));
  }

  useEffect(() => {
    const fetchCards = async () => {
      try {
        const data = await getAllCardUsers();
        if (data.code === 1000) {
          setCards(data.body);
        }
      } catch (err) {
        console.log(err);
      } finally {
        setLoading(false);
      }
    };

    fetchCards();
  }, []);

  if (loading) return <div className="card-list-container">Đang tải...</div>;

  return (
    <div className="card-list-container">
      {filteredList.map((card) => {
        return (
          <CardItem
            setCards={setCards}
            key={card.id}
            {...card}
            handleFilteredListAfterRemoving={handleFilteredListAfterRemoving}
          />
        );
      })}
    </div>
  );
}
