import FormInput from "../Form/FormItem";
import Button from "../Button/Button";
import { changePassword } from "../../service/UserService";
import { useState } from "react";
import SuccessPopup from "../Popup/SuccessPopup";
import ErrorPopup from "../Popup/ErrorPopup";
import DisableLayer from "../Popup/DisableLayer";
import ExitHeader from "../Header/ExitHeader";

export default function ChangePassword({ exitBtn }) {
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);
  const [enableErrorPopup, setEnableErrorPopup] = useState(false);
  const [errMessage, setErrMessage] = useState("");
  const [enableDisableLayer, setEnableDisableLayer] = useState(false);

  async function handleChangPasword() {
    setEnableDisableLayer();
    const currPassword = document.querySelector("#curr-password").value;
    const newPassword = document.querySelector("#new-password").value;
    const confirmPassword = document.querySelector("#confirm-password").value;

    const data = await changePassword(
      currPassword,
      newPassword,
      confirmPassword,
    );

    if (data.code === 1000) {
      setEnableSuccessPopup(true);
      setTimeout(() => {
        setEnableDisableLayer(false);
        setEnableSuccessPopup(false);
        exitBtn();
      }, 2600);
    } else {
      setErrMessage(data.message);
      setEnableErrorPopup(true);
    }
  }

  function exitPopup() {
    setEnableErrorPopup(false);
    setEnableDisableLayer(false);
  }

  function handleSubmit(e) {
    e.preventDefault();
  }

  return (
    <div className="change-password-container">
      <ExitHeader onClick={exitBtn} title="Đổi mật khẩu" />
      <form onSubmit={handleSubmit}>
        <FormInput
          title="Mật khẩu hiện tại"
          type="password"
          id="curr-password"
          className="text-input"
        />
        <FormInput
          title="Mật khẩu mới"
          type="password"
          id="new-password"
          className="text-input"
        />
        <FormInput
          title="Xác nhận mật khẩu"
          type="password"
          id="confirm-password"
          className="text-input"
        />

        <Button
          title="Lưu thay đổi"
          type="login-btn"
          onClick={handleChangPasword}
        />
      </form>
      {enableDisableLayer && <DisableLayer />}
      {enableSuccessPopup && <SuccessPopup onClick={exitPopup} />}
      {enableErrorPopup && (
        <ErrorPopup title="Error" message={errMessage} onClick={exitPopup} />
      )}
    </div>
  );
}
