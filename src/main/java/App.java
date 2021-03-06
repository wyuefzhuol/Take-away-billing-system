import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        //TODO: write code here
        StringBuffer result = new StringBuffer();
        result.append("============= Order details =============\n");
        List<String> idCollect = inputs.stream().map(element->element.substring(0,element.indexOf("x")-1)).collect(Collectors.toList());
        int length = inputs.get(0).length();
        List<Integer> countCollect = inputs.stream().map(element->Integer.valueOf(element.substring(length-1,length))).collect(Collectors.toList());
        List<Item> itemList = itemRepository.findAll();
        List<SalesPromotion> salesPromotionList = salesPromotionRepository.findAll();
        List<String> salesItemList = salesPromotionList.get(1).getRelatedItems();
        List<String> salesItemName = new ArrayList<>();
        int totalPrice = 0,firstPromotionPrice=0,secondPromotionPrice=0;
        for(Item item:itemList){
            for(int i=0;i<inputs.size();i++){
                if(item.getId().equals(idCollect.get(i))){
                    if(salesItemList.contains(idCollect.get(i))){
                        secondPromotionPrice+=(item.getPrice()/2)*countCollect.get(i);
                        salesItemName.add(item.getName());
                    }else {
                        secondPromotionPrice+=item.getPrice()*countCollect.get(i);
                    }
                    totalPrice+=item.getPrice()*countCollect.get(i);
                    firstPromotionPrice+=item.getPrice()*countCollect.get(i);
                    result.append(item.getName()+" x "+countCollect.get(i)+" = "+(int)(item.getPrice()*countCollect.get(i))+" yuan\n");
                    break;
                }
            }
        }
        result.append("-----------------------------------\n");
        if(firstPromotionPrice>=30){
            firstPromotionPrice-=6;
        }
        if(totalPrice<=firstPromotionPrice && totalPrice<=secondPromotionPrice){
            result.append("Total: "+totalPrice+" yuan\n");
            result.append("===================================");
        }else if(firstPromotionPrice<totalPrice && firstPromotionPrice<=secondPromotionPrice){
            result.append("Promotion used:\n");
            result.append("Deduct 6 yuan when the order reaches 30 yuan, saving 6 yuan\n"+"-----------------------------------\n");
            result.append("Total: "+firstPromotionPrice+" yuan\n");
            result.append("===================================");
        }else if(secondPromotionPrice<totalPrice && secondPromotionPrice<firstPromotionPrice){
            result.append("Promotion used:\n");
            result.append("Half price for certain dishes (");
            String saleItem = salesItemName.stream().reduce("",(preElem,curElem)->preElem+","+curElem);
            result.append(saleItem.substring(1));
            result.append(")，saving "+(totalPrice-secondPromotionPrice)+" yuan\n");
            result.append("-----------------------------------\n");
            result.append("Total: "+secondPromotionPrice+" yuan\n");
            result.append("===================================");
        }
        return result.toString();
    }
}
