function FormInput({onChange, title, id, type, className, placeHolder }) {
  return (
    <div>
      <label htmlFor={id}>{title}</label>
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
