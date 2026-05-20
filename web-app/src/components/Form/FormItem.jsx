import { useState } from "react";
import eye from "../../assets/eye-regular-full.svg";
import hiddenEye from "../../assets/eye-slash-regular-full.svg";

function FormInput({
  value,
  onChange,
  title,
  className,
  placeHolder,
  isPasswordField,
  isHiddenPassword,
  setIsHiddenPassword
}) {
 
  const handleHiddenPassword = () => {
    setIsHiddenPassword(!isHiddenPassword);
  };


  return (
    <div>
      <p>{title}</p>
      <input
        value={value ? value : ""}
        className={className}
        type={isHiddenPassword ? "password" : "text"}
        onChange={onChange}
        placeholder={placeHolder}
      />
      {isPasswordField ? (
        <img
          onClick={handleHiddenPassword}
          className="eye-icon"
          src={isHiddenPassword ? eye : hiddenEye}
          alt=""
        />
      ) : null}
    </div>
  );
}

export default FormInput;
