function FormInput({ value, onChange, title, type, className, placeHolder }) {
  return (
    <div>
      <p>{title}</p>
      <input
        value={value ? value : ""}
        className={className}
        type={type}
        onChange={onChange}
        placeholder={placeHolder}
      />
    </div>
  );
}

export default FormInput;
