let count = 1;
let readonly  = true;
const addIngredient = document.querySelector(`#add-ingredient`);
const deleteIngredient = document.querySelector(`#delete-ingredient`);
const ingredientsBox = document.querySelector(`#ingredients`);
const editRecipe = document.querySelector(`.edit`);

addIngredient.addEventListener(`click`, e => {
    e.preventDefault();
    const ingredients = document.querySelector(`#ingredients`);

    const divCol = document.createElement("div");
    const divCard = document.createElement("div");
    const divName = document.createElement("div");
    const inputName = document.createElement("input");
    const spanName = document.createElement("span");
    const divCount = document.createElement("div");
    const inputCount = document.createElement("input");
    const spanCount = document.createElement("span");
    const divUnit = document.createElement("div");
    const divUnitTitle = document.createElement("div");
    const spanUnitTitle = document.createElement("span");
    const selectUnit = document.createElement("select");
    const optionSzt = document.createElement("option");
    const optionKg = document.createElement("option");
    const optionG = document.createElement("option");
    const optionL = document.createElement("option");
    const optionMl = document.createElement("option");

    divCol.classList.add("col-md-3");
    divCol.classList.add("ingredient-" + count);

    divCard.classList.add("card");
    divCard.classList.add("ingredient-" + count);

    divName.classList.add("inputBox");
    inputName.autocomplete = "off";
    inputName.id = "ingredientName" + count;
    inputName.name = "ingredientDtoList[" + count + "].name";
    inputName.type = "text";
    inputName.required = true;
    spanName.innerText = "Nazwa składnika";

    divCount.classList.add("inputBox");
    inputCount.autocomplete = "off";
    inputCount.id = "ingredientCount" + count;
    inputCount.name = "ingredientDtoList[" + count + "].count";
    inputCount.type = "text";
    inputCount.required = true;
    spanCount.innerText = "Ilość";

    divUnit.classList.add("unitsList");
    divUnitTitle.classList.add("units");
    divUnitTitle.classList.add("w-100");
    spanUnitTitle.innerText = "Jednostka";
    selectUnit.id = "ingredientUnit" + count;
    selectUnit.name = "ingredientDtoList[" + count + "].unit";
    optionSzt.value = "szt";
    optionSzt.innerText = "szt"
    optionKg.value = "kg";
    optionKg.innerText = "kg";
    optionG.value = "g";
    optionG.innerText = "g";
    optionL.value = "l";
    optionL.innerText = "l";
    optionMl.value = "ml";
    optionMl.innerText = "ml";
    selectUnit.options.add(optionSzt);
    selectUnit.options.add(optionKg);
    selectUnit.options.add(optionG);
    selectUnit.options.add(optionL);
    selectUnit.options.add(optionMl);

    divName.appendChild(inputName);
    divName.appendChild(spanName);

    divCount.appendChild(inputCount);
    divCount.appendChild(spanCount);

    divUnitTitle.appendChild(spanUnitTitle);
    divUnit.appendChild(divUnitTitle);
    divUnit.appendChild(selectUnit);

    divCard.appendChild(divName);
    divCard.appendChild(divCount);
    divCard.appendChild(divUnit);

    divCol.appendChild(divCard);

    ingredients.appendChild(divCol);

    count++;
})

deleteIngredient.addEventListener(`click`, e => {
    e.preventDefault();
    if (count > 0) {
        count--;
    }
    ingredientsBox.removeChild(ingredientsBox.lastElementChild);
})

editRecipe.addEventListener(`click`, e => {
    e.preventDefault();
    if (readonly === true) {
        readonly = false;
        console.log('Editing');
        editRecipe.innerHTML = "Anuluj zmiany";
        const readOnlyElements = document.querySelectorAll(`.readonly`);
        for (const element of readOnlyElements) {
            element.removeAttribute(`readonly`);
        }
        const disabledElements = document.querySelectorAll(`.disabled`);
        for (const element of disabledElements) {
            element.removeAttribute(`disabled`);
        }
    } else {
        readonly = true;
        console.log('Stopped editing');
        history.back();
    }
})