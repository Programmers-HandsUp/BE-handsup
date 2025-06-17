package dev.handsup.auction.repository.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import dev.handsup.search.domain.AuctionSearch;
import jakarta.transaction.Transactional;

public interface AuctionSearchRepository extends JpaRepository<AuctionSearch, Long>, AuctionSearchQueryRepository {

	@Transactional
	@Modifying
	@Query(
		value = """
        UPDATE auction_search s
        JOIN (
            SELECT a.auction_id,
                   COUNT(DISTINCT b.bookmark_id) AS bookmark_count,
                   COUNT(DISTINCT bd.bidding_id) AS bidding_count
              FROM auction a
              LEFT JOIN bookmark b ON a.auction_id = b.auction_id
              LEFT JOIN bidding bd ON a.auction_id = bd.auction_id
             GROUP BY a.auction_id
        ) cnt ON s.auction_id = cnt.auction_id
        SET s.bookmark_count = cnt.bookmark_count,
            s.bidding_count = cnt.bidding_count
        """,
		nativeQuery = true
	)
	void updateAuctionSearch();
}