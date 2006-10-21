/**
 * Copyright 2006 gworks.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. (http://www.apache.org/licenses/LICENSE-2.0)
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed 
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
 * the specific language governing permissions and limitations under the License.
 *
 * <dhamma-message src="Atisha (11th century Tibetan Buddhist master)">
 *  The greatest achievement is selflessness. The greatest worth is self-mastery.
 *  The greatest quality is seeking to serve others. The greatest precept is continual awareness.
 *  The greatest medicine is the emptiness of everything. The greatest action is not conforming with the worlds ways.
 *  The greatest magic is transmuting the passions. The greatest generosity is non-attachment.
 *  The greatest goodness is a peaceful mind. The greatest patience is humility.
 *  The greatest effort is not concerned with results. The greatest meditation is a mind that lets go.
 *  The greatest wisdom is seeing through appearances. 
 * </dhamma-message>
 * 
 * @author Ashin Wimalajeewa (ash)
 */
package au.com.gworks.gwt.petstore.client;

import java.util.Map;

import org.javaongems.gwk.client.AbstractPageController;
import org.javaongems.gwk.client.Card;
import org.javaongems.gwk.client.CardDeck;
import org.javaongems.win.client.OutlookBar;
import org.javaongems.win.client.OutlookBarItem;
import org.javaongems.win.client.OutlookBarList;

import au.com.gworks.gwt.petstore.client.service.AisleInfo;
import au.com.gworks.gwt.petstore.client.service.ItemInfo;
import au.com.gworks.gwt.petstore.client.service.ItemRefInfo;
import au.com.gworks.gwt.petstore.client.service.ProductInfo;
import au.com.gworks.gwt.petstore.client.service.ShoppingRpcController;
import au.com.gworks.gwt.petstore.client.service.UrlBaseRequestInfo;
import au.com.gworks.gwt.petstore.client.service.UrlItemRequestInfo;
import au.com.gworks.gwt.petstore.client.service.UrlProductRequestInfo;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Widget;

public class ShoppingController extends AbstractPageController {
	final static public String CAT_ID = "cat-id";
	final static public String PROD_ID = "prod-id";
	final static public String ITEM_ID = "item-id";
	
	final static private String AISLE_INDEX= "aisle-index";
	final static private String PRODUCT_INDEX= "product-index";
	final static private String ITEM_CARD= "item-card";
	final static private String RESULT = "result";
	final static private String PRODUCTS_ARR = "products-arr";

	
	private ShoppingView view;
	private AisleListener aisleListener = new AisleListener();
	private ShelfListener shelfListener = new ShelfListener();
	
	private AisleInfo[] aisles;
	private ProductInfo[] products;
	private ItemRefInfo[] shelfRefs;
	
	private boolean openned = false;
	private boolean bypassEvent = false;
	
	public void setUp() {
		view = new ShoppingView();
		setPagePanel(StoreCoordinator.instance.getPagePanel());
		StoreCoordinator.instance.registerPageController(view, this);
		view.setUp(aisleListener, shelfListener);
	}

	public void loadStoreAisles() {
		ControllerCall cb = new ControllerCall() {
			public void onSuccess(Object result) {
				listStoreAisles(result);		
			}
		};
		ShoppingRpcController.Util.getInstance().listStoreAisles(cb);
	}

	private void loadAislesProducts(int idx) {
		if (idx == -1)
			return;

		String catId = aisles[idx].id;
		ControllerCall cb = new ControllerCall(AISLE_INDEX, new Integer(idx)) {
			public void onSuccess(Object result) {
				listAislesProducts(result, this.state);		
			}
		};
		ShoppingRpcController.Util.getInstance().listAislesProducts(catId, cb);
	}
	
	private void loadProductShelf(int idx) {
		if (idx == -1)
			return;

		String prodId = products[idx].id;
		ControllerCall cb = new ControllerCall(PRODUCT_INDEX, new Integer(idx)) {
			public void onSuccess(Object result) {
				listProductShelf(result, this.state);		
			}
		};
		cb.state.put(PRODUCTS_ARR, products);
		ShoppingRpcController.Util.getInstance().listProductShelf(prodId, cb);
	}
	
	private void loadShelfItemDetails(Card sender) {
		ProductCardView card = (ProductCardView) sender;
		ItemRefInfo sri = card.getShelfRefInfo();
		ControllerCall cb = new ControllerCall(ITEM_CARD, card) {
			public void onSuccess(Object result) {
				listShelfItemDetails(result, this.state);		
			}
		};
		ShoppingRpcController.Util.getInstance().listShelfItemDetails(sri.id, cb);
	}
	
	private void loadCompleteDetailsForUrlItemRequest(String itemId) {
		ControllerCall cb = new ControllerCall(ITEM_ID, itemId) {
			public void onSuccess(final Object result) {
				final Map theState = this.state;
				DeferredCommand.add(new Command() {
					public void execute() {
						listCompleteDetailsForUrlItemRequest(result, theState);		
					}
				});
			}
		};
		ShoppingRpcController.Util.getInstance().listCompleteDetailsForUrlItemRequest(itemId, cb);
	}

	private void loadCompleteDetailsForUrlProductRequest(String prodId) {
		ControllerCall cb = new ControllerCall(PROD_ID, prodId) {
			public void onSuccess(final Object result) {
				final Map theState = this.state;
				DeferredCommand.add(new Command() {
					public void execute() {
						listCompleteDetailsForUrlProductRequest(result, theState);		
					}
				});
			}
		};
		ShoppingRpcController.Util.getInstance().listCompleteDetailsForUrlProductRequest(prodId, cb);
	}
	
	private void listStoreAisles(Object result) {
		aisles = (AisleInfo[]) result;
		view.loadAisles(aisles);
	}
	
	private void listAislesProducts(Object result, Map callState) {
		ProductInfo[] resProds = (ProductInfo[]) result;
		products = resProds;
		Integer idx = (Integer) callState.get(AISLE_INDEX);
		view.loadProducts(idx.intValue(), resProds);
		if (!bypassEvent)
			loadProductShelf(0);
//		if (Gem.stone.isDebugEnabled()) {
//			Gem.stone.log("listAislesProducts: idx=" + idx + ", resProds.len=" + resProds.length + ", byP=" + bypassEvent);
//			for (int i = 0; i < resProds.length; i++) 
//				Gem.stone.log("__" + i + "." + resProds[i].description);	
//		}
	}
	
	private void listProductShelf(Object result, Map callState) {
		ItemRefInfo[] resShelves = (ItemRefInfo[]) result;
		shelfRefs = resShelves;
		Integer idx = (Integer) callState.get(PRODUCT_INDEX);
		ProductInfo[] callProds = (ProductInfo[]) callState.get(PRODUCTS_ARR); 
		ProductInfo productInfo = callProds[idx.intValue()];	
		view.loadShelf(productInfo, resShelves);
//		if (Gem.stone.isDebugEnabled()) {
//			Gem.stone.log("listProductShelf: idx=" + idx + ", callProds.len=" + callProds.length + ", productInfo.id=" + productInfo.id + ", resShelves.len=" +resShelves.length);
//			for (int i = 0; i < callProds.length; i++) 
//				Gem.stone.log("__" + i + "." + callProds[i].description);	
//			for (int i = 0; i < resShelves.length; i++) 
//				Gem.stone.log("__" + i + "." + resShelves[i].description);	
//		}
	}
	
	private void listShelfItemDetails(Object result, Map callState) {
		ItemInfo detail = (ItemInfo) result;
		ProductCardView card = (ProductCardView) callState.get(ITEM_CARD);
		card.setShelfRefInfo(detail);
	}
	
	private void listCompleteDetailsForUrlItemRequest(Object result, Map callState) {
		UrlItemRequestInfo data = (UrlItemRequestInfo) result;
		callState.put(RESULT, result);
		bypassEvent = true;
		try {
			listCompleteDetailsBase(callState, data);
			callState.put(PRODUCT_INDEX, new Integer(findProductIndex(data.products, data.productId)));
//			shelfRefs = data.items;	// ASW
			callState.put(PRODUCTS_ARR, data.products);
			listProductShelf(data.items, callState);
			ProductCardView card = view.findAndOpenProductCard(data.nominatedItem.id);
			card.setShelfRefInfo(data.nominatedItem);
		} finally {
			bypassEvent = false;
		}
	}

	private void listCompleteDetailsForUrlProductRequest(Object result, Map callState) {
		UrlProductRequestInfo data = (UrlProductRequestInfo) result;
		String prodId = (String) callState.get(PROD_ID);
		callState.put(RESULT, result);
		bypassEvent = true;
		try {
			listCompleteDetailsBase(callState, data);
		} finally {
			bypassEvent = false;
		}
		loadProductShelf(findProductIndex(data.products, prodId));
	}
	
	private void listCompleteDetailsBase(Map callState, UrlBaseRequestInfo data) {
		int aisleIdx = findAisleIndex(data.aisleId);
		callState.put(AISLE_INDEX, new Integer(aisleIdx));
		products = data.products;
		listAislesProducts(data.products, callState);
		view.openAisle(aisleIdx);
	}

	private int findAisleIndex(String id) {
		for (int i = 0; i < aisles.length; i++) {
			if (aisles[i].id.equals(id))
				return i;
		}
		return -1;
	}

	private int findProductIndex(ProductInfo[] prods, String prodId) {
		for (int i = 0; i < prods.length; i++) {
			if (prods[i].id.equals(prodId))
				return i;
		}
		return -1;
	}
	
	protected class AisleListener extends OutlookBar.ListenerAdapter {
		public void onItemSelected(int idx, OutlookBarItem sender) {
			loadProductShelf(idx);
		}
		
		public void onCategoryClose(int idx, OutlookBarList list) {
			list.clear();
		}
		
		public void onCategoryOpen(int idx, OutlookBarList list) {
			if (!bypassEvent)
				loadAislesProducts(idx);
		}
	}
	
	protected class ShelfListener extends CardDeck.ListenerAdapter {
		public void onCardOpenned(Card sender) {
			if (!bypassEvent)
				loadShelfItemDetails(sender);
		}
	}
	
	// template methods
	public void mountPageView() {
		pagePanel.add(view, null, "Shopping", false);
	}
	
	public void onContainerResized(int width, int height) {
		int bodyWh = pagePanel.getPageBodyOffsetWidth();
		int bodyHt = pagePanel.getPageBodyOffsetHeight();
//		view.setPixelSize(bodyWh, bodyHt);
		view.refresh();
	}
	
	protected void preparePageForQuery(Map queryMap) {
		if (queryMap.size() == 0)
			return;
		String itemId = (String) queryMap.get(ITEM_ID);
		String prodId = (String) queryMap.get(PROD_ID);
		if (itemId != null)  
			loadCompleteDetailsForUrlItemRequest(itemId);
		else if (prodId != null) 
			loadCompleteDetailsForUrlProductRequest(prodId);
	}

	protected Widget getPageView() {
		return view;
	}
	
	protected String getPageName() {
		return "shopping";
	}
	
	protected void openPage() {
		if (!openned) {
			loadStoreAisles();
			openned = true;
		}
		Widget w = StoreCoordinator.instance.getCartController().getSummaryView();
		pagePanel.setPageContext(w);
		super.openPage();
	}
	
	protected void onControllerCallSuccess(ControllerCall call, Object result) {
		// handled by inner classes
	}
}