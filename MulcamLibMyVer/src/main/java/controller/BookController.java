package controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import biz.book.service.BookService;
import restAPI.Book;
import restAPI.BookAPI;


@Controller
public class BookController {
	
	@Autowired
	BookService service;
	@RequestMapping("/booklist.do")
	public String board(HttpServletRequest request) throws Exception {
		String target = request.getParameter("option");
		String keyword = request.getParameter("target");
		HttpSession session = request.getSession();
		session.setAttribute("option",target);
		session.setAttribute("target",keyword);
		System.out.println(target+" "+keyword);
		ArrayList<Book> bookList = BookAPI.searchBook(target,keyword);
		request.setAttribute("booklist", bookList);
		return "/book/book_list";
	}
	
	@RequestMapping(value = "/book.do", method = RequestMethod.POST)
	public ModelAndView book(Book book,int check) {
		System.out.println(book);
		ModelAndView mav = new ModelAndView();
		mav.addObject("book", book);
		mav.addObject("check",check);
		mav.setViewName("/book/book_view");
		return mav;
	}
}