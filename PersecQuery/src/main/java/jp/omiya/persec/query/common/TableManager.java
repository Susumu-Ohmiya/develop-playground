package jp.omiya.persec.query.common;

public class TableManager {
	public static Table forName(String name) {

		Tables t = Tables.of(name);

		if (t == null) {
			return null;
		}

		return t.actualClass;


	}

	// FIXME : コンポーネントスキャンとかのようなやり方でやった方が良い
	public static enum Tables {
		SERVANT("servant", "jp.omiya.persec.query.entity.DemoTable");

		public String getQueryName() {
			return queryName;
		}

		public Table getActualClass() {
			return actualClass;
		}

		private String queryName;
		private Table actualClass;

		private Tables(String name, String className) {
			queryName = name;
			try {
				actualClass = (Table) Class.forName(className).getMethod("getInstance", (Class<?>[]) null).invoke(null, (Object[]) null);
			} catch (IllegalArgumentException | ReflectiveOperationException | SecurityException e) {
				e.printStackTrace();
			}
		}

		public static Tables of(String name) {
			if (name == null) {
				return null;
			}

			for (Tables t : Tables.values()) {
				if (t.getQueryName().equals(name)) {
					return t;
				}
			}
			return null;
		}
	}
}
