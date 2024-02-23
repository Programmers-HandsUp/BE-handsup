package dev.handsup.bookmark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bookmark.domain.Bookmark;
import dev.handsup.user.domain.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	Optional<Bookmark> findByUserAndAuction(User user, Auction auction);

	Boolean existsByUserAndAuction(User user, Auction auction);
}
