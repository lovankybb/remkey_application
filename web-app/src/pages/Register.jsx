import FormInput from "../components/Form/FormItem.jsx";
import Button from "../components/Button/Button.jsx";
import DisableLayer from "../components/Popup/DisableLayer.jsx";
import { useState, useEffect } from "react";
import ErrorPopup from "../components/Popup/ErrorPopup.jsx";
import OtpVerifyPopup from "../components/Popup/OtpVerifyPopup.jsx";
import register from "../service/RegisterService.js";
import verifyUserCreation from "../service/VerifyUserCreation.js";
import SuccessPopup from "../components/Popup/SuccessPopup.jsx";

export default function Register() {
  const [isDisableLayer, setDisableLayer] = useState(false);
  const [enableErrPopup, setEnableErrPopup] = useState(false);
  const [enableOtpVerifyPopup, setEnableOtpVerifyPopup] = useState(false);
  const [errMessage, setErrMessage] = useState("");
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);

  useEffect(() => {
    document.title="Đăng ký tài khoản"; 
    document.body.style.backgroundImage =
      "url('src/assets/login-background.jpg')";
    return () => {
      document.body.style.backgroundImage = "";
      document.title=""; 
    };
  });

  async function handleRegister() {
    setDisableLayer(true);
    const username = document.querySelector("#username").value;
    const password = document.querySelector("#password").value;
    const email = document.querySelector("#email").value;

    const data = await register(username, password, email);

    if (data.code === 1000) {
      setEnableOtpVerifyPopup(true);
    } else {
      setErrMessage(data.message);
      setEnableErrPopup(true);
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault();
  };

  function exitErrorPopup() {
    setDisableLayer(false);
    setEnableErrPopup(false);
  }

  async function submitOtp() {
    setEnableOtpVerifyPopup(false);
    const email = document.querySelector("#email").value;
    const otpCode = document.querySelector("#otp-code").value;

    const data = await verifyUserCreation(email, otpCode);

    if (data.code === 1000) {
      setEnableSuccessPopup(true);
      setTimeout(() => {
        setDisableLayer(false);
        setEnableSuccessPopup(false);
      }, 2600);
    } else {
      setErrMessage(data.message);
      setEnableErrPopup(true);
    }
  }

  function exitOtpVerifyPopup() {
    setDisableLayer(false);
    setEnableOtpVerifyPopup(false);
  }

  return (
    <>
      <div className="form-container" id="register-form-container">
        <img className="logo-inform" src="/src/assets/logo.png" alt="" />
        <form className="register-form" onSubmit={handleSubmit}>
          <FormInput
            className="text-input"
            id="email"
            type="email"
            title="Email"
          />
          <FormInput
            className="text-input"
            id="username"
            type="text"
            title="Tên đăng nhập"
          />
          <FormInput
            className="text-input"
            id="password"
            type="password"
            title="Mật khẩu"
          />
          <Button
            type="blue-btn"
            title="Đăng ký tài khoản"
            onClick={handleRegister}
          />
        </form>
        <div>
          <a href="./login">Đăng nhập</a>
        </div>
      </div>
      {isDisableLayer && <DisableLayer />}
      {enableErrPopup && (
        <ErrorPopup
          title="Error"
          onClick={exitErrorPopup}
          message={errMessage}
        />
      )}
      {enableOtpVerifyPopup && (
        <OtpVerifyPopup cancel={exitOtpVerifyPopup} submit={submitOtp} />
      )}

      {enableSuccessPopup && <SuccessPopup />}
    </>
  );
}
