import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import backgroundImage from "../assets/login-background.jpg";
import logo from "../assets/logo.png";
import Button from "../components/Button/Button.jsx";
import FormInput from "../components/Form/FormItem.jsx";
import DisableLayer from "../components/Popup/DisableLayer.jsx";
import ErrorPopup from "../components/Popup/ErrorPopup.jsx";
import login from "../service/LoginService.js";

export default function Login() {
  const [isDisableLayer, setDisableLayer] = useState(false);
  const [errMessage, setErrMessage] = useState("");
  const [errTitle, setErrTitle] = useState("");
  const [enableErrPopup, setEnableErrPopup] = useState(false);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  useEffect(() => {
    document.body.style.backgroundImage = `url(${backgroundImage})`;

    document.title = "Đăng nhập";
    return () => {
      document.body.style.backgroundImage = "";
      document.title = "";
    };
  });

  async function handleLogin() {
    console.log("username", username);
    console.log("password", password);

    setDisableLayer(true);
    const result = await login(username, password);

    if (result !== null) {
      setErrTitle("Error");
      setErrMessage(result.message);
      setEnableErrPopup(true);
    }
    if (localStorage.getItem("jwtToken") != null) window.location.href = "/";
  }

  const exitErrorPopup = () => {
    setDisableLayer(false);
    setEnableErrPopup(false);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
  };

  return (
    <>
      <div className="form-container">
        <img className="logo-inform" src={logo} alt="" />
        <form className="login-form" onSubmit={handleSubmit}>
          <FormInput
            className="text-input"
            id="username"
            type="text"
            title="Tên đăng nhập"
            onChange={(e) => setUsername(e.target.value.trim())}
          />
          <FormInput
            className="text-input"
            id="password"
            type="password"
            title="Mật khẩu"
            onChange={(e) => setPassword(e.target.value.trim())}
          />
          <Button
            onClick={handleLogin}
            type="blue-btn"
            title="Đăng nhập"
          ></Button>
        </form>
        <div>
          <Link to="/register">Đăng ký tài khoản</Link>
        </div>
      </div>
      {isDisableLayer && <DisableLayer />}
      {enableErrPopup && (
        <ErrorPopup
          title={errTitle}
          message={errMessage}
          onClick={exitErrorPopup}
        />
      )}
    </>
  );
}
