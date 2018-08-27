package kr.co.marueng.mtalk.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 곽현호
 * @DATE 2018. 7. 4.
 * @param <T>
 */
@Alias("pagingVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingVO<T> implements Serializable {
	private long totalRecord;
	private int screenSize=5;
	private int blockSize=5;
	private long totalPage;
	private int currentPage;
	private int startRow;
	private int endRow;
	private long startPage;
	private long endPage;
	private T searchVO;
	private List<T> dataList; 
	private String searchType;
	private String searchWord;
	
	
	

	public PagingVO(int screenSize, int blockSize) {
		super();
		this.screenSize = screenSize;
		this.blockSize = blockSize;
	}
	
	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
		totalPage = (long)Math.ceil(totalRecord/(double)screenSize);
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		endRow = currentPage * screenSize;
		startRow = endRow - (screenSize-1);
		startPage = ((currentPage-1)/blockSize)*blockSize+1;
		endPage =  startPage+(blockSize-1);
		
	}

	public String getPagingHTML(){
		StringBuffer html = new StringBuffer("<nav><ul class='pagination'>");
		String pattern = "<li class='page-item  %s'><a class='page-link' href='?page=%d' data-page='%d'>[%s]</a></li>";
		if(startPage > 1){
			html.append(String.format(pattern, "", (startPage-blockSize), (startPage-blockSize), "Previous"));
		}else{
			html.append(String.format(pattern, "disabled", (startPage-blockSize), (startPage-blockSize), "Previous"));
		}
		for(long pageNum = startPage; 
				pageNum <= (endPage > totalPage ? totalPage : endPage);
				pageNum++){
			if(pageNum == currentPage){
				html.append(String.format(pattern, "active", pageNum, pageNum, pageNum));
			}else{
				html.append(String.format(pattern, "", pageNum, pageNum, pageNum));
			}
		}
		if(endPage < totalPage){
			html.append(String.format(pattern, "", (endPage+1), (endPage+1), "Next"));
		}else{
			html.append(String.format(pattern, "disabled", (endPage+1), (endPage+1), "Next"));
		}
		html.append("</ul></nav>");
		return html.toString();
	}
}
