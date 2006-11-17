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
package au.com.gworks.gwt.petstore.client.service;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BaseItemRefInfo implements IsSerializable {
	/**
	 * convenient client-side cache. content is this cache
	 * is not intended to be sent of the wire.
	 */
	/*transient*/ public Map cargo = new HashMap(); 
	public String id;
	public double listPrice;

	public BaseItemRefInfo() {
	}
	
	public BaseItemRefInfo(String id, double listPrice) {
		this.id = id;
		this.listPrice = listPrice;
	}
	
	public void assign(BaseItemRefInfo src) {
		this.id = src.id;
		this.listPrice = src.listPrice;
		this.cargo.putAll(src.cargo);
	}
}
