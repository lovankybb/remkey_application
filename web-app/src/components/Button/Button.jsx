export default function Button({ type, title, onClick }) {
  return (
    <button onClick={onClick} className={type}>
      {title}
    </button>
  );
}
