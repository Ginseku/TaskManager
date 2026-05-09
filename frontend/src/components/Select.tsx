import Select from 'react-select';
import { select_options } from '../mock/select_options';

const SelectMenu = (props : any) => {
    return (
        <Select
            isMulti
            name="colors"
            options={props.options}
            onChange={ props.setSelectedOptions }
            className="basic-multi-select"
            classNamePrefix="select"
        />
    );
}

export default SelectMenu;