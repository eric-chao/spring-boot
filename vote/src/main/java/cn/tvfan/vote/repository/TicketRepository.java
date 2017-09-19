package cn.tvfan.vote.repository;

import cn.tvfan.vote.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}
