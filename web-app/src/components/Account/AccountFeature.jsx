export function AccountFeature({title, onClick}){
    return (
        <li className="account-feature" onClick={onClick}>{title} <img src="/src/assets/chevron-right-solid-full.svg" alt="" />
        </li>
    )
}