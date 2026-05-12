function FormInput({ title, id, type, className, placeHolder }) {
  return (
    <div>
      <label htmlFor={id}>{title}</label>
      <input
        className={className}
        type={type}
        id={id}
        placeholder={placeHolder}
      />
    </div>
  );
}

export default FormInput;
