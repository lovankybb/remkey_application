import Button from "../Button/Button.jsx";

export default function OtpVerifyPopup({ cancel, submit }) {
  return (
    <div className="otp-verify-popup">
      <h1>Vui lòng kiểm tra email</h1>
      <input type="text" id="otp-code" />
      <div>
        <Button title="Cancel" type="exit-otp-verify-btn" onClick={cancel} />
        <Button title="Gửi" type="submit-otp-verify-btn" onClick={submit} />
      </div>
    </div>
  );
}
