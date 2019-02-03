package jp.omiya.persec.query;

import java.util.List;

import org.jparsec.ParseTree;
import org.jparsec.error.ParserException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jp.omiya.persec.query.ast.Relation;
import jp.omiya.persec.query.common.Config;
import jp.omiya.persec.query.common.QueryRecord;
import jp.omiya.persec.query.common.QueryUtil;
import jp.omiya.persec.query.parser.RelationParser;

public class Demo {

	public static void main(String[] args) {

		Config.LOGGING_EVALUATION = false;

		try {
			Relation r = RelationParser.ready().parse("select servant where (effect = 'attackup_rate' OR target = 'party') and amount > (20 * (4 + -2)) and skill1 is not null order by rarity desc, id asc", Config.PARSER_MODE);
			QueryUtil.log(r.getSql());

			List<QueryRecord> queryResult = r.evaluate();

			ObjectMapper m = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
			QueryUtil.log(m.writeValueAsString(queryResult));

			for (QueryRecord queryRecord : queryResult) {
				QueryUtil.log(queryRecord);
			}
		} catch (ParserException e) {
			e.printStackTrace();
			ParseTree tree = e.getParseTree();
			QueryUtil.log(tree);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
