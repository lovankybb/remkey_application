import FormInput from "../components/Form/FormItem.jsx";
import Button from "../components/Button/Button.jsx";
import DisableLayer from "../components/Popup/DisableLayer.jsx";
import { useState, useEffect } from "react";
import ErrorPopup from "../components/Popup/ErrorPopup.jsx";
import OtpVerifyPopup from "../components/Popup/OtpVerifyPopup.jsx";
import register from "../service/RegisterService.js";
import verifyUserCreation from "../service/VerifyUserCreation.js";
import SuccessPopup from "../components/Popup/SuccessPopup.jsx";
import backgroundImage from "../assets/login-background.jpg";
import logo from "../assets/logo.png";
import { Link } from "react-router-dom";

export default function Register() {
  const [isDisableLayer, setDisableLayer] = useState(false);
  const [enableErrPopup, setEnableErrPopup] = useState(false);
  const [enableOtpVerifyPopup, setEnableOtpVerifyPopup] = useState(false);
  const [errMessage, setErrMessage] = useState("");
  const [enableSuccessPopup, setEnableSuccessPopup] = useState(false);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [otpCode, setOtpCode] = useState("");

   const [isHiddenPassword, setIsHiddenPassword] = useState(true);

  useEffect(() => {
    document.title = "Đăng ký tài khoản";
    document.body.style.backgroundImage = `url(${backgroundImage})`;
    return () => {
      document.body.style.backgroundImage = "";
      document.title = "";
    };
  });

  async function handleRegister() {
    setDisableLayer(true);

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
        <img className="logo-inform" src={logo} alt="" />
        <form className="register-form" onSubmit={handleSubmit}>
          <FormInput
            className="text-input"
            type="email"
            title="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value.trim())}
          />
          <FormInput
            className="text-input"
            type="text"
            title="Tên đăng nhập"
            value={username}
            onChange={(e) => setUsername(e.target.value.trim())}
          />
          <FormInput
            isPasswordField={true}
            setIsHiddenPassword={setIsHiddenPassword}
            isHiddenPassword={isHiddenPassword}
            value={password}
            className="text-input"
            type="password"
            title="Mật khẩu"
            onChange={(e) => setPassword(e.target.value.trim())}
          />
          <Button
            type="blue-btn"
            title="Đăng ký tài khoản"
            onClick={handleRegister}
          />
        </form>
        <div>
          <Link to="/login">Đăng nhập</Link>
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
        <OtpVerifyPopup
          cancel={exitOtpVerifyPopup}
          submit={submitOtp}
          onChange={(e) => setOtpCode(e.target.value.trim())}
        />
      )}

      {enableSuccessPopup && <SuccessPopup />}
    </>
  );
}
