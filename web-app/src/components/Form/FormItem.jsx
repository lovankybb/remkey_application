function FormInput({onChange, title, type, className, placeHolder }) {
  return (
    <div>
      <p >{title}</p>
      <input
        className={className}
        type={type}
        onChange={onChange}
        placeholder={placeHolder}
      />
    </div>
  );
}

export default FormInput;
