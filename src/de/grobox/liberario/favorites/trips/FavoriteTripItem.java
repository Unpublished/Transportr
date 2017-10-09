/*    Transportr
 *    Copyright (C) 2013 - 2016 Torsten Grote
 *
 *    This program is Free Software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.grobox.liberario.favorites.trips;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.Date;

import javax.annotation.ParametersAreNonnullByDefault;

import de.grobox.liberario.data.searches.StoredSearch;
import de.grobox.liberario.locations.WrapLocation;

import static com.google.common.base.Preconditions.checkArgument;
import static de.grobox.liberario.favorites.trips.FavoriteTripType.HOME;
import static de.grobox.liberario.favorites.trips.FavoriteTripType.TRIP;
import static de.grobox.liberario.favorites.trips.FavoriteTripType.WORK;

@ParametersAreNonnullByDefault
public class FavoriteTripItem extends StoredSearch implements Comparable<FavoriteTripItem> {

	private final FavoriteTripType type;
	private final @Nullable WrapLocation from, via, to;

	public FavoriteTripItem(long uid, WrapLocation from, @Nullable WrapLocation via, WrapLocation to) {
		this.uid = uid;
		this.type = TRIP;
		this.from = from;
		this.via = via;
		this.to = to;
	}

	public FavoriteTripItem(StoredSearch storedSearch, WrapLocation from, @Nullable WrapLocation via, WrapLocation to) {
		super(storedSearch);
		this.type = TRIP;
		this.from = from;
		this.via = via;
		this.to = to;
	}

	public FavoriteTripItem(FavoriteTripType type, @Nullable WrapLocation to) {
		checkArgument(type == HOME || type == WORK, "This constructor can only be used for HOME and WORK");
		this.type = type;
		this.from = null;
		this.via = null;
		this.to = to;
		this.count = 0;
		this.lastUsed = new Date();
		this.favorite = false;
	}

	FavoriteTripType getType() {
		return type;
	}

	@Nullable
	public WrapLocation getFrom() {
		return from;
	}

	@Nullable
	public WrapLocation getVia() {
		return via;
	}

	@Nullable
	public WrapLocation getTo() {
		return to;
	}

	@Override
	public int compareTo(FavoriteTripItem i) {
		if (equals(i)) return 0;
		if (type == HOME) return -1;
		if (i.type == HOME) return 1;
		if (type == WORK) return -1;
		if (i.type == WORK) return 1;
		if (favorite && !i.favorite) return -1;
		if (!favorite && i.favorite) return 1;
		if (favorite) {
			if (count == i.count) return lastUsed.compareTo(i.lastUsed);
			return count > i.count ? -1 : 1;
		} else {
			return lastUsed.compareTo(i.lastUsed) * -1;
		}
	}

	@Override
	@SuppressWarnings("RedundantIfStatement")
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof FavoriteTripItem))
			return false;
		final FavoriteTripItem other = (FavoriteTripItem) o;
		if (!Objects.equal(this.type, other.type))
			return false;
		if (!Objects.equal(this.from, other.from))
			return false;
		if (!Objects.equal(this.to, other.to))
			return false;
		if (!Objects.equal(this.via, other.via))
			return false;
		return true;
	}

	@SuppressWarnings("RedundantIfStatement")
	boolean equalsAllFields(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof FavoriteTripItem))
			return false;
		final FavoriteTripItem other = (FavoriteTripItem) o;
		if (!Objects.equal(this.type, other.type))
			return false;
		if (!Objects.equal(this.from, other.from))
			return false;
		if (!Objects.equal(this.to, other.to))
			return false;
		if (!Objects.equal(this.via, other.via))
			return false;
		if (this.count != other.count)
			return false;
		if (this.favorite != other.favorite)
			return false;
		return true;
	}

}
